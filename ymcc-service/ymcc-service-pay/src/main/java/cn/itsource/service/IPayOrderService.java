package cn.itsource.service;

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
}
