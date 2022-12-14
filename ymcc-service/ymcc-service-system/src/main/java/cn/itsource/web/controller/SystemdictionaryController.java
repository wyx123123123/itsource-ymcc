package cn.itsource.web.controller;

import cn.itsource.service.ISystemdictionaryService;
import cn.itsource.domain.Systemdictionary;
import cn.itsource.query.SystemdictionaryQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/systemdictionary")
public class SystemdictionaryController {

    @Autowired
    public ISystemdictionaryService systemdictionaryService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody Systemdictionary systemdictionary){
        if(systemdictionary.getId()!=null){
            systemdictionaryService.updateById(systemdictionary);
        }else{
            systemdictionaryService.insert(systemdictionary);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        systemdictionaryService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(systemdictionaryService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(systemdictionaryService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody SystemdictionaryQuery query){
        Page<Systemdictionary> page = new Page<Systemdictionary>(query.getPage(),query.getRows());
        page = systemdictionaryService.selectPage(page);
        return JsonResult.success(new PageList<Systemdictionary>(page.getTotal(),page.getRecords()));
    }
}
