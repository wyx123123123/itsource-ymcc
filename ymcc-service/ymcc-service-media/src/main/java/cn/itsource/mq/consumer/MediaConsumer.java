package cn.itsource.mq.consumer;

import cn.itsource.domain.MediaFile;
import cn.itsource.service.IMediaFileService;
import cn.itsource.service.impl.MediaFileServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RocketMQMessageListener(
        consumerGroup="service-media-consumer",//消费者组名
        topic="topic-media",//topic
        selectorExpression="tag-media",//tag
        messageModel= MessageModel.CLUSTERING   //消费消息的模式是集群：一个消息只会被一个消费者消息
        // 还有广播模式： 一个消息可以被多个人同时消费
)
public class MediaConsumer implements RocketMQListener<MessageExt> {
    @Autowired
    private IMediaFileService mediaFileService;

    //消费消息
    @Override
    public void onMessage(MessageExt messageExt) {
        if(messageExt.getBody() == null){//防疫性编程
            return;
        }
        //真正的消费消息的业务逻辑
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        MediaFile mediaFile = JSONObject.parseObject(message, MediaFile.class);

        mediaFileService.handleFile2m3u8(mediaFile);
    }
}
