package cn.itsource.ffmpeg;


import cn.itsource.MediaApp;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MediaApp.class)
public class ProductTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Test
    public void test() throws InterruptedException {
        Message<String> message = MessageBuilder.withPayload("我是王大锤！！").build();
        rocketMQTemplate.sendOneWay("topic-media:tag-media", message);

        Thread.sleep(5000);
    }
    
    
}
