package cn.itsource.service.impl;

import cn.itsource.domain.AlipayInfo;
import cn.itsource.domain.PayOrder;
import cn.itsource.dto.ApplyPayDto;
import cn.itsource.service.IAlipayInfoService;
import cn.itsource.service.IPayOrderService;
import cn.itsource.service.IPayService;
import cn.itsource.util.AssertUtil;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 统一支付
 */
@Service
public class PayServiceImpl implements IPayService {
    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private IAlipayInfoService alipayInfoService;

    @Override
    public String apply(ApplyPayDto dto) {
        Wrapper<PayOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("order_no",dto.getOrderNo());
        PayOrder payOrder = payOrderService.selectOne(wrapper);
        AssertUtil.isNotNull(payOrder,"支付单不存在！！");

        Integer payType = dto.getPayType().intValue();
        // 支付方式:0余额直接，1支付宝，2微信,3银联
        if(payType == 1){//支付宝支付
            return alipay(payOrder,dto);
        }else if (payType == 2){//微信
            //....
        }
        return null;
    }

    public String alipay(PayOrder payOrder,ApplyPayDto dto){
        AlipayInfo alipayInfo = alipayInfoService.selectList(null).get(0);
        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions(alipayInfo));
        try {
            // 2. 发起API调用 网页支付
            AlipayTradePagePayResponse response = Factory.Payment.Page().pay(
                    payOrder.getSubject(),//标题
                    payOrder.getOrderNo(),//订单号
                    payOrder.getAmount().toString(),//金额
                    StringUtils.hasLength(dto.getCallUrl()) ? dto.getCallUrl() : alipayInfo.getReturnUrl() //同步回调地址
            );
            // 3. 处理响应或异常
            if (ResponseChecker.success(response)) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    //设置商家的相关参数
    /**
     * 收款方的参数
     * @return
     */
    public Config getOptions(AlipayInfo alipayInfo) {
        Config config = new Config();
        config.protocol = alipayInfo.getProtocol();
        config.gatewayHost = alipayInfo.getGatewayHost();//openapi.alipaydev.com
        config.signType = alipayInfo.getSignType();
        config.appId = alipayInfo.getAppId();
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = alipayInfo.getMerchantPrivateKey();
        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = alipayInfo.getAlipayPublicKey();
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = alipayInfo.getNotifyUrl();// 后台的支付服务的controller接口
        return config;
    }
}
