package cn.itsource.service;

import cn.itsource.domain.CourseOrder;
import cn.itsource.dto.OrderParamDto;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author director
 * @since 2022-09-16
 */
public interface ICourseOrderService extends IService<CourseOrder> {

    /**
     * 执行普通订单下单
     * @param dto
     */
    String placeOrder(OrderParamDto dto);
}
