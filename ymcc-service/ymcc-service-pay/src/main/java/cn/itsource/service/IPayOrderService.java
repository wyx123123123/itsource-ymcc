package cn.itsource.service;

import cn.itsource.domain.PayFlow;
import cn.itsource.domain.PayOrder;
import cn.itsource.dto.Order2PayOrderParamDto;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author director
 * @since 2022-09-18
 */
public interface IPayOrderService extends IService<PayOrder> {

    /**
     * 消费消息，保存支付单
     * @param orderDto
     */
    void savePayOrder(Order2PayOrderParamDto orderDto);

    /**
     * 修改支付单&保存支付流水
     * @param payOrder
     * @param payFlow
     */
    void updatePayOrderAndInsertPayFlow(PayOrder payOrder, PayFlow payFlow);

    /**
     * 取消支付单并关闭支付宝交易
     * @param orderNo
     */
    void payTimeOutCancelOrder(String orderNo);
}
