package cn.itsource.service.impl;

import cn.itsource.domain.AlipayInfo;
import cn.itsource.domain.PayFlow;
import cn.itsource.domain.PayOrder;
import cn.itsource.dto.AlipayNotifyDto;
import cn.itsource.dto.ApplyPayDto;
import cn.itsource.dto.PayResultDto;
import cn.itsource.service.IAlipayInfoService;
import cn.itsource.service.IPayFlowService;
import cn.itsource.service.IPayOrderService;
import cn.itsource.service.IPayService;
import cn.itsource.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一支付
 */
@Service
@Slf4j
public class PayServiceImpl implements IPayService {
    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private IAlipayInfoService alipayInfoService;
    @Autowired
    private IPayFlowService payFlowService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;


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

    /**
     * 1.验证签名，确保是支付宝给我们发的
     * 2.校验订单是否正确 支付金额是否正确===业务校验
     * 3.根据支付宝的支付状态必须是交易成功，执行下面
     * 4.支付服务
     *   。修改支付单状态+时间
     *   。记录支付流水
     * 5.订单服务+课程服务==通过Mq去完成
     *  。发送事务消息==
     *  。由消费者去广播消费消息即可
     * @param dto
     * @return
     */
    @Override
    public String notify(AlipayNotifyDto dto) {
        // 1.验证签名，确保是支付宝给我们发的
        String jsonStr = JSON.toJSONString(dto);
        Map map = JSON.parseObject(jsonStr, Map.class);
        try {
            //如果结果是true，说明验签成功
            Boolean isValid = Factory.Payment.Common().verifyNotify(map);
            if(!isValid){
                return "NO";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "NO";
        }

        //==== 走到这里说明验证签名成功------
        Wrapper<PayOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("order_no",dto.getOut_trade_no());
        PayOrder payOrder = payOrderService.selectOne(wrapper);
        AssertUtil.isNotNull(payOrder,"支付单不存在！！！");
        // 2.校验订单是否正确 支付金额是否正确===业务校验
        boolean isEq = payOrder.getAmount().compareTo(new BigDecimal(dto.getTotal_amount())) == 0;
        AssertUtil.isTrue(isEq,"支付金额不匹配！！");
        // 3.根据支付宝的支付状态必须是交易成功，执行下面
        AssertUtil.isTrue(dto.isTradeSuccess(),"支付宝的支付状态不合法！！");
        if(payOrder.getPayStatus() == PayOrder.STATE_CANCEL){
           //退款
            try {
                Factory.Payment.Common().refund(payOrder.getOrderNo(), payOrder.getAmount().toString());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("支付宝退款异常！orderNo:{},请检查！！",payOrder.getOrderNo());
            }

            return "success";
        }
        // 4.支付服务
        //   。修改支付单状态+时间
        Date now = new Date();
        payOrder.setPayStatus(PayOrder.STATE_PAY_SUCCESS);
        payOrder.setUpdateTime(now);
        //payOrderService.updateById(payOrder);//这里直接修改有没有问题   交给Mq的事务监听器
        //   。记录支付流水
        PayFlow flow = new PayFlow();
        flow.setNotifyTime(now);
        flow.setSubject(payOrder.getSubject());
        flow.setOutTradeNo(payOrder.getOrderNo());
        flow.setTotalAmount(payOrder.getAmount());
        flow.setTradeStatus(dto.getTrade_status());
        flow.setPaySuccess(true);
        //payFlowService.insert(flow);//这里也不应该直接保存  交给Mq的事务监听器
        HashMap<String, Object> payMap = new HashMap<>();
        payMap.put("payOrder",payOrder);
        payMap.put("payFlow",flow);

        // 5.订单服务+课程服务==通过Mq去完成
        //  。发送事务消息==
        //  。由消费者去广播消费消息即可
        // orderNo loginId   courseIds
        PayResultDto resultDto = new PayResultDto(
                payOrder.getOrderNo(),
                payOrder.getExtParams()//这里的值要追溯到下单服务的 发送保存支付当的事务消息哪里
        );

        Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(resultDto)).build();
        rocketMQTemplate.sendMessageInTransaction(
                "TxPayResultListener",
                "topic-payresult:tag-payresult",
                message,
                payMap
        );

        return "success";
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
