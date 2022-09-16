package cn.itsource.web.controller;

import cn.itsource.dto.CourseDto;
import cn.itsource.service.ICourseService;
import cn.itsource.domain.Course;
import cn.itsource.query.CourseQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import cn.itsource.vo.CourseInfoVo;
import cn.itsource.vo.OrderInfoVo;
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
     * 查询出多个课程的相关信息，用来填充订单结算页
     * @param courseIds
     * @return
     */
    @RequestMapping(value="/info/{courseIds}",method= RequestMethod.GET)
    public JsonResult orderInfo(@PathVariable("courseIds") String courseIds){
        OrderInfoVo orderInfo = courseService.orderInfo(courseIds);
        return JsonResult.success(orderInfo);
    }

    /**
     * 检查当前登录人能否观看此视频
     * @param mediaId
     * @return
     */
    @RequestMapping(value="/getForUser/{mediaId}",method= RequestMethod.GET)
    public JsonResult getForUser(@PathVariable("mediaId") Long mediaId){
        String url = courseService.getForUser(mediaId);
        return JsonResult.success(url);
    }

    /**
     * 根据课程ID查询课程详情
     */
    @RequestMapping(value="/detail/data/{courseId}",method= RequestMethod.GET)
    public JsonResult courseInfo(@PathVariable("courseId") Long courseId){
        CourseInfoVo vo = courseService.courseInfo(courseId);
        return JsonResult.success(vo);
    }

    /**
     * 发布课程
     * @return
     */
    @RequestMapping(value="/onLineCourse/{courseId}",method= RequestMethod.POST)
    public JsonResult onLineCourse(@PathVariable("courseId") Long courseId){
        courseService.onLineCourse(courseId);
        return JsonResult.success();
    }


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
