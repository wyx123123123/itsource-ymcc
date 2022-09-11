package cn.itsource.web.controller;

import cn.itsource.dto.CourseDto;
import cn.itsource.service.ICourseService;
import cn.itsource.domain.Course;
import cn.itsource.query.CourseQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    public ICourseService courseService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult save(@RequestBody @Valid CourseDto courseDto){
        courseService.save(courseDto);
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        courseService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(courseService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(courseService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseQuery query){
        Page<Course> page = new Page<Course>(query.getPage(),query.getRows());
        Wrapper<Course> wrapper = new EntityWrapper<>();
        wrapper.like("name", query.getKeyword());
        page = courseService.selectPage(page, wrapper);
        return JsonResult.success(new PageList<Course>(page.getTotal(),page.getRecords()));
    }
}