package cn.itsource.mq.listener;

import cn.itsource.domain.CourseOrder;
import cn.itsource.domain.PayFlow;
import cn.itsource.domain.PayOrder;
import cn.itsource.dto.Order2PayOrderParamDto;
import cn.itsource.dto.PayResultDto;
import cn.itsource.service.IPayOrderService;
import cn.itsource.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 发送事务消息的事务监听器
 */
@RocketMQTransactionListener(txProducerGroup = "TxPayResultListener")// 必须要和发送消息的组名一致
public class TxPayResultMqListener implements RocketMQLocalTransactionListener {
    @Autowired
    private IPayOrderService payOrderService;

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
            AssertUtil.isNotNull(o,"数据为空，保存本地数据失败！！");
            Map<String,Object> map = (Map) o;
            PayOrder payOrder = (PayOrder)map.get("payOrder");
            PayFlow payFlow = (PayFlow) map.get("payFlow");
            payOrderService.updatePayOrderAndInsertPayFlow(payOrder,payFlow);
            return RocketMQLocalTransactionState.COMMIT;//执行本地事务成功
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;//本地事务执行失败，回滚
        }
    }

    /**
     * 检查本地事务  校验支付单是否是已支付
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        try {
            byte[] bytes = (byte[]) message.getPayload();
            String jsonStr = new String(bytes, StandardCharsets.UTF_8);
            PayResultDto dto = JSON.parseObject(jsonStr, PayResultDto.class);

            Wrapper<PayOrder> wrapper = new EntityWrapper<>();
            wrapper.eq("order_no",dto.getOrderNo());
            PayOrder payOrder = payOrderService.selectOne(wrapper);
            if(payOrder.getPayStatus() == PayOrder.STATE_PAY_SUCCESS){
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
