package cn.itsource.service;

import cn.itsource.domain.CourseOrder;
import cn.itsource.dto.OrderParamDto;
import cn.itsource.dto.PayResultDto;
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

    /**
     * 保存主订单和子订单
     * @param courseOrder
     */
    void saveOrderAndItems(CourseOrder courseOrder);

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    CourseOrder selectByOrderNo(String orderNo);

    /**
     * 处理支付结果
     * @param orderDto
     */
    void payResultHandle(PayResultDto orderDto);
}
