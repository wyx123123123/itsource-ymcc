package cn.itsource.service.impl;

import cn.itsource.domain.CourseOrder;
import cn.itsource.domain.PayFlow;
import cn.itsource.domain.PayOrder;
import cn.itsource.dto.Order2PayOrderParamDto;
import cn.itsource.mapper.PayOrderMapper;
import cn.itsource.service.IPayFlowService;
import cn.itsource.service.IPayOrderService;
import cn.itsource.util.AssertUtil;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.common.models.AlipayTradeCloseResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author director
 * @since 2022-09-18
 */
@Service
@Slf4j
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {
    @Autowired
    private IPayFlowService payFlowService;

    @Override
    public void savePayOrder(Order2PayOrderParamDto orderDto) {
        PayOrder payOrder = new PayOrder();
        BeanUtils.copyProperties(orderDto,payOrder);

        payOrder.setCreateTime(new Date());
        payOrder.setPayStatus(PayOrder.STATE_WAITE_PAY);//待支付
        insert(payOrder);
    }

    @Override
    public void updatePayOrderAndInsertPayFlow(PayOrder payOrder, PayFlow payFlow) {
        PayOrder tmp = selectById(payOrder.getId());
        //处理消息的幂等问题
        boolean isWaitPay = tmp.getPayStatus() == PayOrder.STATE_WAITE_PAY;
        AssertUtil.isTrue(isWaitPay,"支付单状态不合法！！！");
        updateById(payOrder);
        payFlowService.insert(payFlow);
    }

    @Override
    public void payTimeOutCancelOrder(String orderNo) {
        Wrapper<PayOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("order_no",orderNo);
        PayOrder payOrder = selectOne(wrapper);
        if(payOrder == null){
            return;//下次不要告诉我了
        }
        boolean isWaitPay = payOrder.getPayStatus() == PayOrder.STATE_WAITE_PAY;
        if(!isWaitPay){
            return;//下次不要告诉我了
        }
        log.info("支付超时取消支付单{}",orderNo);
        //取消支付单
        payOrder.setPayStatus(PayOrder.STATE_CANCEL);
        updateById(payOrder);
        //关闭支付宝交易
        try {
            Factory.Payment.Common().close(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("哦豁，调用支付包关闭交易失败，但是呢：不要理它！！！我们通过支付宝异步通知中去退款来解决");
        }
    }
}
