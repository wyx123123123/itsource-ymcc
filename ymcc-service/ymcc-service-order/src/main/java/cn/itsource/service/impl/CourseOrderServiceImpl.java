package cn.itsource.service.impl;

import cn.itsource.domain.Course;
import cn.itsource.domain.CourseMarket;
import cn.itsource.domain.CourseOrder;
import cn.itsource.domain.CourseOrderItem;
import cn.itsource.dto.OrderParamDto;
import cn.itsource.mapper.CourseOrderMapper;
import cn.itsource.result.JsonResult;
import cn.itsource.service.ICourseOrderItemService;
import cn.itsource.service.ICourseOrderService;
import cn.itsource.util.AssertUtil;
import cn.itsource.util.CodeGenerateUtils;
import cn.itsource.vo.OrderInfoVo;
import cn.itsource.vo.OrderItemInfoVo;
import cn.itsource.feign.CourseFeignClient;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author director
 * @since 2022-09-16
 */
@Service
public class CourseOrderServiceImpl extends ServiceImpl<CourseOrderMapper, CourseOrder> implements ICourseOrderService {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private CourseFeignClient courseFeignClient;
    @Autowired
    private ICourseOrderItemService courseOrderItemService;

    /**
     * 1.参数校验
     * 2.防重token校验
     *   拼接key从redis获取token
     *     获取不到，直接报错
     *     获取到了，但是比对值不一致，报错
     * 下单====
     * 3.查询多个课程 + 销售
     *   判断状态是否合法
     * 4.按照课程以一个的封装订单详情
     * 5.封装主订单
     * 6.保存数据
     * 7.删除redis中的防重复token
     *
     *
     * =======支付相关===
     * @TODO 支付
     *
     * @param dto
     */
    @Override
    public String placeOrder(OrderParamDto dto) {
        // 1.参数校验  JSR303
        // 2.防重token校验
        List<Long> courseIds = dto.getCourseIds();
        String courseIdsStr  = StringUtils.join(courseIds, ",");
        Long loginId = 3L;// @TODO
        //   拼接key从redis获取token
        String key = "token:"+loginId+":"+courseIdsStr;
        Object tokenTmp = redisTemplate.opsForValue().get(key);
        //     获取不到，直接报错
        AssertUtil.isNotNull(tokenTmp,"token失效，请重新下单");
        //     获取到了，但是比对值不一致，报错
        AssertUtil.isEquals(dto.getToken(),tokenTmp.toString(),"骚年，你是不是想搞事！！");
        // 下单====
        // 3.查询多个课程 + 销售
           // 为课程服务编写controller接口 已经写好了，公用 订单结算页渲染接口
           // 为课程服务编写api-course
           // 通过feign调用课程服务，获得课程+销售信息
        JsonResult jsonResult = courseFeignClient.orderInfo(courseIdsStr);
        AssertUtil.isTrue(jsonResult.isSuccess(),"下单失败！未查询到课程!");
        Object data = jsonResult.getData();
        AssertUtil.isNotNull(data,"下单失败！未查询到课程!");
        String voStr = JSON.toJSONString(data);
        OrderInfoVo orderInfoVo = JSON.parseObject(voStr, OrderInfoVo.class);
        List<OrderItemInfoVo> courseInfos = orderInfoVo.getCourseInfos();

        //创建主订单
        // 5.封装主订单

        CourseOrder order = new CourseOrder();
        Date now = new Date();
        order.setCreateTime(now);
        order.setOrderNo(CodeGenerateUtils.generateOrderSn(loginId));
        order.setTotalCount(1);
        order.setStatusOrder(CourseOrder.STATE_WAITE_PAY);
        order.setUserId(loginId);
        order.setPayType(dto.getPayType());
        // 6.保存数据
        insert(order);//保存订单

        StringBuffer title = new StringBuffer();
        //创建子订单
        title.append("课程：【");
        for (OrderItemInfoVo courseInfo : courseInfos) {
            Course course = courseInfo.getCourse();
            CourseMarket courseMarket = courseInfo.getCourseMarket();
            CourseOrderItem item = new CourseOrderItem();
            item.setOrderId(order.getId());
            item.setAmount(courseMarket.getPrice());
            item.setCount(1);
            item.setCreateTime(now);
            item.setCourseId(course.getId());
            item.setCourseName(course.getName());
            item.setCoursePic(course.getPic());
            item.setOrderNo(order.getOrderNo());
            // 6.保存数据
            courseOrderItemService.insert(item);
            title.append(course.getName());
            order.setTotalAmount(order.getTotalAmount().add(courseMarket.getPrice()));//累加子订单的钱钱
        }
        title.append("】订单");
        order.setTitle(title.toString());
        updateById(order);

        // 7.删除redis中的防重复token
        redisTemplate.delete(key);

        // =======支付相关===
        // @TODO 支付
        return order.getOrderNo();
    }
}
