package cn.itsource.service.impl;

import cn.itsource.domain.PayOrder;
import cn.itsource.dto.Order2PayOrderParamDto;
import cn.itsource.mapper.PayOrderMapper;
import cn.itsource.service.IPayOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
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

    @Override
    public void savePayOrder(Order2PayOrderParamDto orderDto) {
        PayOrder payOrder = new PayOrder();
        BeanUtils.copyProperties(orderDto,payOrder);

        payOrder.setCreateTime(new Date());
        payOrder.setPayStatus(PayOrder.STATE_WAITE_PAY);//待支付
        insert(payOrder);
    }
}
