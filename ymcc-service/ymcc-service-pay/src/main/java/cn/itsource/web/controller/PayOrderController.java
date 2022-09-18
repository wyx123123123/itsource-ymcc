package cn.itsource.web.controller;

import cn.itsource.service.IPayOrderService;
import cn.itsource.domain.PayOrder;
import cn.itsource.query.PayOrderQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payOrder")
public class PayOrderController {

    @Autowired
    public IPayOrderService payOrderService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody PayOrder payOrder){
        if(payOrder.getId()!=null){
            payOrderService.updateById(payOrder);
        }else{
            payOrderService.insert(payOrder);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        payOrderService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(payOrderService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(payOrderService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody PayOrderQuery query){
        Page<PayOrder> page = new Page<PayOrder>(query.getPage(),query.getRows());
        page = payOrderService.selectPage(page);
        return JsonResult.success(new PageList<PayOrder>(page.getTotal(),page.getRecords()));
    }
}
