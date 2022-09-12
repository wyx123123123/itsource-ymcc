package cn.itsource.mq.producer;

import cn.itsource.domain.MediaFile;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 异步推流 消息生产者
 */
@Component
public class MediaProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public boolean sendMessage(MediaFile mediaFile){
        String jsonStr = JSON.toJSONString(mediaFile);
        Message<String> message = MessageBuilder.withPayload(jsonStr).build();
        //同步发送消息
        SendResult sendResult = rocketMQTemplate.syncSend("topic-media:tag-media", message);
        SendStatus sendStatus = sendResult.getSendStatus();
        return sendStatus.equals(SendStatus.SEND_OK);

    }

}
