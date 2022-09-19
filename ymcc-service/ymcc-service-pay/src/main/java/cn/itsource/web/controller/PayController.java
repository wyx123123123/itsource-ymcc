package cn.itsource.web.controller;

import cn.itsource.domain.PayOrder;
import cn.itsource.dto.AlipayNotifyDto;
import cn.itsource.dto.ApplyPayDto;
import cn.itsource.result.JsonResult;
import cn.itsource.service.IPayOrderService;
import cn.itsource.service.IPayService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private IPayOrderService payOrderService;

    @Autowired
    private IPayService payService;

    /**
     * 检查支付单是否已经存在
     * @param orderNo
     * @return  true 存在，false不存在
     */
    @RequestMapping(value="/checkPayOrder/{orderNo}",method= RequestMethod.GET)
    public JsonResult saveOrUpdate(@PathVariable("orderNo") String orderNo){
        Wrapper<PayOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("order_no",orderNo);
        PayOrder payOrder = payOrderService.selectOne(wrapper);
        return payOrder != null ? JsonResult.success() : JsonResult.error();
    }

    /**
     * 发起支付申请
     * @param dto
     * @return
     */
    @RequestMapping(value="/apply",method= RequestMethod.POST)
    public JsonResult apply(@RequestBody ApplyPayDto dto){
        //service有没有返回值
        String payResponse = payService.apply(dto);
        return JsonResult.success(payResponse);
    }

    /**
     * 支付宝支付异步回调通知接口
     * @param dto
     * @return 如果底层业务处理完成，返回：success 字符串给支付宝
     *        如果返回的不是success，那么支付宝还会再次通知你，直到8次你都没有响应success，支付宝就放弃通知你了
     */
    @RequestMapping(value="/alipay/notify")
    public String notify(AlipayNotifyDto dto){
        //service有没有返回值
        try {
            log.info("支付宝支付异步回调了啊啊啊啊啊啊啊啊！！！");
            return payService.notify(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return "NO";//还会给我发
        }
    }

}
