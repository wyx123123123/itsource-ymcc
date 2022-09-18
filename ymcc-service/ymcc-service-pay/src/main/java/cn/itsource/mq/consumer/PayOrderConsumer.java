package cn.itsource.mq.consumer;

import cn.itsource.domain.PayOrder;
import cn.itsource.dto.Order2PayOrderParamDto;
import cn.itsource.service.IPayOrderService;
import cn.itsource.service.impl.PayOrderServiceImpl;
import cn.itsource.util.AssertUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RocketMQMessageListener(
        consumerGroup="service-pay-consumer",//消费者组名
        topic="topic-order",//topic
        selectorExpression="tag-order",//tag
        messageModel= MessageModel.CLUSTERING   //消费消息的模式是集群：一个消息只会被一个消费者消息
        // 还有广播模式： 一个消息可以被多个人同时消费
)
public class PayOrderConsumer implements RocketMQListener<MessageExt> {
    @Autowired
    private IPayOrderService payOrderService;


    //消费消息

    /**
     *  消费消息一定要处理幂等性  自动签收消息
     * 只要我们的方法不抛出异常，MQ就默认为消费成功，那么就会标记该消息为已消费，不在投递了
     *   如果我们方法中抛出了异常，那么MQ就认为你没有消费成功，我过一会儿还会投递这个消息给你
     * @param messageExt
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        if(messageExt.getBody() == null){//防御性编程
            return;
        }
        //真正的消费消息的业务逻辑
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        Order2PayOrderParamDto orderDto = JSONObject.parseObject(message, Order2PayOrderParamDto.class);

        //我们需要保证消费消息的幂等性   一个消息只能成功消费一次，只能保存一个支付单
        Wrapper<PayOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("order_no",orderDto.getOrderNo());
        PayOrder payOrder = payOrderService.selectOne(wrapper);
        if(payOrder != null){
            return;//自动签收，以后不要再发给我了
        }
        payOrderService.savePayOrder(orderDto);
    }
}
