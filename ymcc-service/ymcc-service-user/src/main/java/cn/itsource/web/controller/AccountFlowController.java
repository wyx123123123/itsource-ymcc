package cn.itsource.web.controller;

import cn.itsource.service.IAccountFlowService;
import cn.itsource.domain.AccountFlow;
import cn.itsource.query.AccountFlowQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accountFlow")
public class AccountFlowController {

    @Autowired
    public IAccountFlowService accountFlowService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody AccountFlow accountFlow){
        if(accountFlow.getId()!=null){
            accountFlowService.updateById(accountFlow);
        }else{
            accountFlowService.insert(accountFlow);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        accountFlowService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(accountFlowService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(accountFlowService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody AccountFlowQuery query){
        Page<AccountFlow> page = new Page<AccountFlow>(query.getPage(),query.getRows());
        page = accountFlowService.selectPage(page);
        return JsonResult.success(new PageList<AccountFlow>(page.getTotal(),page.getRecords()));
    }
}
