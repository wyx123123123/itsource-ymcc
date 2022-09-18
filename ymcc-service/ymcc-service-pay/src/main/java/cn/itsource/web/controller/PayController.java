package cn.itsource.web.controller;

import cn.itsource.domain.PayOrder;
import cn.itsource.dto.ApplyPayDto;
import cn.itsource.result.JsonResult;
import cn.itsource.service.IPayOrderService;
import cn.itsource.service.IPayService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay")
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
}
