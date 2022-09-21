package cn.itsource.service.impl;

import cn.itsource.domain.Course;
import cn.itsource.domain.CourseMarket;
import cn.itsource.domain.CourseOrder;
import cn.itsource.domain.CourseOrderItem;
import cn.itsource.dto.Order2PayOrderParamDto;
import cn.itsource.dto.OrderParamDto;
import cn.itsource.dto.PayResultDto;
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
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
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
@Slf4j
public class CourseOrderServiceImpl extends ServiceImpl<CourseOrderMapper, CourseOrder> implements ICourseOrderService {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private CourseFeignClient courseFeignClient;
    @Autowired
    private ICourseOrderItemService courseOrderItemService;

    @Autowired
    private RocketMQTemplate rocketMqTemplate;

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
        //insert(order);//保存订单  交给Mq的事务监听器去保存本地事务

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
            //courseOrderItemService.insert(item); 交给Mq的事务监听器去保存本地事务
            order.getItems().add(item);
            title.append(course.getName());
            order.setTotalAmount(order.getTotalAmount().add(courseMarket.getPrice()));//累加子订单的钱钱
        }
        title.append("】订单");
        order.setTitle(title.toString());
        //updateById(order);  交给Mq的事务监听器去保存本地事务
        //主订单和子订单都有完整信息了
        /*
          message(消息体): 用来生成支付单的参数  订单号 支付金额 支付方式 标题  userId 扩展参数
          arg(扩展参数): 使用扩展参数，传递主订单和子订单，方便执行本地事务
         */
        // =======支付相关===   发送事务消息，让支付服务消费消息保存支付单
        HashMap<String, Object> map = new HashMap<>();
        map.put("loginId",loginId);
        map.put("courseIds",courseIdsStr);

        Order2PayOrderParamDto paramDto = new Order2PayOrderParamDto(
                order.getTotalAmount(),
                dto.getPayType(),
                order.getOrderNo(),
                loginId,
                JSON.toJSONString(map),//扩展参数
                order.getTitle()
        );
        String jsonString = JSON.toJSONString(paramDto);
        Message<String> message = MessageBuilder.withPayload(jsonString).build();
        TransactionSendResult transactionSendResult = rocketMqTemplate.sendMessageInTransaction(
                "TxOrderGroupListener",
                "topic-order:tag-order",
                message,//消息体，我们传递什么？
                order// 扩展参数，用来干啥
        );
        LocalTransactionState localTransactionState = transactionSendResult.getLocalTransactionState();//本地事务的执行状态
        SendStatus sendStatus = transactionSendResult.getSendStatus();//消息发送状态
        boolean isSuccess = localTransactionState != LocalTransactionState.COMMIT_MESSAGE || sendStatus != SendStatus.SEND_OK;
        AssertUtil.isFalse(isSuccess,"下单失败！！！");


        //支付超时取消 延迟消息
        try {
            SendResult sendResult = rocketMqTemplate.syncSend(
                    "topic-paytimeout:tag-paytimeout",
                    MessageBuilder.withPayload(order.getOrderNo()).build(),
                    3000,
                    4// 30秒
            );
            boolean isDelayOk = sendResult.getSendStatus() == SendStatus.SEND_OK;
            if(!isDelayOk){
                //兜底
                //1.重试3次
                //2.记录数据库日志
                //3.发各种通知信息到代码负责的人，运维人员。。。人工介入操作
            }
        } catch (Exception e) {
            e.printStackTrace();
            //兜底
            //1.重试3次
            //2.记录数据库日志
            //3.发各种通知信息到代码负责的人，运维人员。。。人工介入操作
        }


        // 7.删除redis中的防重复token
        redisTemplate.delete(key);
        return order.getOrderNo();
    }

    @Override
    @Transactional
    public void saveOrderAndItems(CourseOrder courseOrder) {
        CourseOrder courseOrderTmp = selectByOrderNo(courseOrder.getOrderNo());
        AssertUtil.isNull(courseOrderTmp,"订单已经存在！！");
        //保存主订单
        insert(courseOrder);
        //订单ID就有了
        List<CourseOrderItem> items = courseOrder.getItems();
        items.forEach(item->{
            //设置子订单的主订单ID
            item.setOrderId(courseOrder.getId());
        });
        //批量保存子订单
        courseOrderItemService.insertBatch(items);
    }

    @Override
    public CourseOrder selectByOrderNo(String orderNo) {
        Wrapper<CourseOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("order_no",orderNo);
        return selectOne(wrapper);
    }

    @Override
    public void payResultHandle(PayResultDto orderDto) {
        CourseOrder courseOrder = selectByOrderNo(orderDto.getOrderNo());
        if(courseOrder ==  null){
            /*
               兜底：
                1.重试
                2.记录日志
                3.发送各种通知短信，让相关人员知晓，可以人工介入弥补
             */
            return;//订单不存在，这个消息你不要给我推了
        }
        boolean isWaitePay = courseOrder.getStatusOrder() == CourseOrder.STATE_WAITE_PAY;
        if(!isWaitePay){
            return;//订单状态不是待支付，你也不要给我推了
        }
        courseOrder.setStatusOrder(CourseOrder.STATE_PAY_SUCCESS);
        courseOrder.setUpdateTime(new Date());
        updateById(courseOrder);
    }

    @Override
    public void payTimeOutCancelOrder(String orderNo) {
        //要做业务幂等性处理  只有是待支付的的订单才能取消
        //====扩展，可能支付宝已经支付成功了，但是还没有异步同志到我们--主动去查询支付宝
        /*
          只要状态是待支付，我就可以放心的取消订单
          如果后续支付宝有异步支付成功的通知过来了，直接退款。。。  我使用它
         */
        CourseOrder order = selectByOrderNo(orderNo);
        if(order == null){
            return;//下次不要告诉我了
        }
        boolean isWaitPay = order.getStatusOrder() == CourseOrder.STATE_WAITE_PAY;
        if(!isWaitPay){
            return;//下次不要告诉我了
        }
        log.info("支付超时取消订单{}",orderNo);
        order.setStatusOrder(CourseOrder.STATE_CANCEL);
        updateById(order);
    }
}
