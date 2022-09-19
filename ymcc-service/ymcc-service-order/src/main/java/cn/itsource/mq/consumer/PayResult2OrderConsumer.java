package cn.itsource.mq.consumer;

import cn.itsource.domain.PayOrder;
import cn.itsource.dto.Order2PayOrderParamDto;
import cn.itsource.dto.PayResultDto;
import cn.itsource.service.ICourseOrderService;
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
        consumerGroup="service-order-payresult-consumer",//消费者组名
        topic="topic-payresult",//topic
        selectorExpression="tag-payresult",//tag
        messageModel= MessageModel.BROADCASTING   //广播模式： 一个消息可以被多个人同时消费
)
public class PayResult2OrderConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private ICourseOrderService courseOrderService;

    //消费消息
    /**
     *  只要不报错，就会自动签收消息
     * @param messageExt
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        if(messageExt.getBody() == null){//防御性编程
            return;
        }
        //真正的消费消息的业务逻辑
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        PayResultDto orderDto = JSONObject.parseObject(message, PayResultDto.class);
        courseOrderService.payResultHandle(orderDto);
    }
}
