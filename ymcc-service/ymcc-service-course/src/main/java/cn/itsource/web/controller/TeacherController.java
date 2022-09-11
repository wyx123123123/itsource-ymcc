package cn.itsource.web.controller;

import cn.itsource.service.ITeacherService;
import cn.itsource.domain.Teacher;
import cn.itsource.query.TeacherQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    public ITeacherService teacherService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody Teacher teacher){
        if(teacher.getId()!=null){
            teacherService.updateById(teacher);
        }else{
            teacherService.insert(teacher);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        teacherService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(teacherService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(teacherService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody TeacherQuery query){
        Page<Teacher> page = new Page<Teacher>(query.getPage(),query.getRows());
        page = teacherService.selectPage(page);
        return JsonResult.success(new PageList<Teacher>(page.getTotal(),page.getRecords()));
    }
}
