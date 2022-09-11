package cn.itsource.web.controller;

import cn.itsource.service.ILoginLogService;
import cn.itsource.domain.LoginLog;
import cn.itsource.query.LoginLogQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loginLog")
public class LoginLogController {

    @Autowired
    public ILoginLogService loginLogService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody LoginLog loginLog){
        if(loginLog.getId()!=null){
            loginLogService.updateById(loginLog);
        }else{
            loginLogService.insert(loginLog);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        loginLogService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(loginLogService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(loginLogService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody LoginLogQuery query){
        Page<LoginLog> page = new Page<LoginLog>(query.getPage(),query.getRows());
        page = loginLogService.selectPage(page);
        return JsonResult.success(new PageList<LoginLog>(page.getTotal(),page.getRecords()));
    }
}
