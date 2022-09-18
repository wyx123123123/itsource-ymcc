package cn.itsource.web.controller;

import cn.itsource.service.IPayFlowService;
import cn.itsource.domain.PayFlow;
import cn.itsource.query.PayFlowQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payFlow")
public class PayFlowController {

    @Autowired
    public IPayFlowService payFlowService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody PayFlow payFlow){
        if(payFlow.getId()!=null){
            payFlowService.updateById(payFlow);
        }else{
            payFlowService.insert(payFlow);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        payFlowService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(payFlowService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(payFlowService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody PayFlowQuery query){
        Page<PayFlow> page = new Page<PayFlow>(query.getPage(),query.getRows());
        page = payFlowService.selectPage(page);
        return JsonResult.success(new PageList<PayFlow>(page.getTotal(),page.getRecords()));
    }
}
