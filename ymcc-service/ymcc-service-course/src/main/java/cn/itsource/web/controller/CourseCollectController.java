package cn.itsource.web.controller;

import cn.itsource.service.ICourseCollectService;
import cn.itsource.domain.CourseCollect;
import cn.itsource.query.CourseCollectQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courseCollect")
public class CourseCollectController {

    @Autowired
    public ICourseCollectService courseCollectService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseCollect courseCollect){
        if(courseCollect.getId()!=null){
            courseCollectService.updateById(courseCollect);
        }else{
            courseCollectService.insert(courseCollect);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        courseCollectService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(courseCollectService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(courseCollectService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseCollectQuery query){
        Page<CourseCollect> page = new Page<CourseCollect>(query.getPage(),query.getRows());
        page = courseCollectService.selectPage(page);
        return JsonResult.success(new PageList<CourseCollect>(page.getTotal(),page.getRecords()));
    }
}
