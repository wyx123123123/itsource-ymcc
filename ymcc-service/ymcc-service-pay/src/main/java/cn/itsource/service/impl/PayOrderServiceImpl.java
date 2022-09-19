package cn.itsource.service.impl;

import cn.itsource.domain.PayFlow;
import cn.itsource.domain.PayOrder;
import cn.itsource.dto.Order2PayOrderParamDto;
import cn.itsource.mapper.PayOrderMapper;
import cn.itsource.service.IPayFlowService;
import cn.itsource.service.IPayOrderService;
import cn.itsource.util.AssertUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
}
