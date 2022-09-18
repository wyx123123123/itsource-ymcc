package cn.itsource.mq;

import cn.itsource.domain.CourseOrder;
import cn.itsource.dto.Order2PayOrderParamDto;
import cn.itsource.service.ICourseOrderService;
import cn.itsource.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;

/**
 * 发送事务消息的事务监听器
 */
@RocketMQTransactionListener(txProducerGroup = "TxOrderGroupListener")// 必须要和发送消息的组名一致
public class TxOrderMqListener  implements RocketMQLocalTransactionListener {
    @Autowired
    private ICourseOrderService courseOrderService;

    /**
     * 执行本地事务
     * @param message
     * @param o
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        //需要在这里保存主订单 &子订单
        try {
            AssertUtil.isNotNull(o,"订单数据为空，保存订单失败！！");
            CourseOrder courseOrder = (CourseOrder) o;
            courseOrderService.saveOrderAndItems(courseOrder);
            return RocketMQLocalTransactionState.COMMIT;//执行本地事务成功
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;//本地事务执行失败，回滚
        }
    }

    /**
     * 检查本地事务  检查订单是否保存成功
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        try {
            byte[] bytes = (byte[]) message.getPayload();
            String jsonStr = new String(bytes, StandardCharsets.UTF_8);
            Order2PayOrderParamDto order2PayOrderParamDto = JSON.parseObject(jsonStr, Order2PayOrderParamDto.class);
            CourseOrder courseOrder = courseOrderService.selectByOrderNo(order2PayOrderParamDto.getOrderNo());
            if(courseOrder != null){
                return RocketMQLocalTransactionState.COMMIT;//说明执行本地事务已经是成功了的
            }else{
                return RocketMQLocalTransactionState.ROLLBACK;//说明执行本地事务没有执行成功
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 记录各种失败日志（消息内容--表），各种通知运维人员，各种通知开发人员【ELK--钉钉-短信-邮件】  由人工接入处理
            return RocketMQLocalTransactionState.UNKNOWN; //我也不知道成功没有
        }



    }
}
