package cn.itsource.web.controller;

import cn.itsource.service.IUserBaseInfoService;
import cn.itsource.domain.UserBaseInfo;
import cn.itsource.query.UserBaseInfoQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userBaseInfo")
public class UserBaseInfoController {

    @Autowired
    public IUserBaseInfoService userBaseInfoService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody UserBaseInfo userBaseInfo){
        if(userBaseInfo.getId()!=null){
            userBaseInfoService.updateById(userBaseInfo);
        }else{
            userBaseInfoService.insert(userBaseInfo);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        userBaseInfoService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(userBaseInfoService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(userBaseInfoService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody UserBaseInfoQuery query){
        Page<UserBaseInfo> page = new Page<UserBaseInfo>(query.getPage(),query.getRows());
        page = userBaseInfoService.selectPage(page);
        return JsonResult.success(new PageList<UserBaseInfo>(page.getTotal(),page.getRecords()));
    }
}
