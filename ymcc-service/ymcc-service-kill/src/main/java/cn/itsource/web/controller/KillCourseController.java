package cn.itsource.web.controller;

import cn.itsource.dto.KillParamDto;
import cn.itsource.service.IKillCourseService;
import cn.itsource.domain.KillCourse;
import cn.itsource.query.KillCourseQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/killCourse")
public class KillCourseController {

    @Autowired
    public IKillCourseService killCourseService;


    @RequestMapping(value="/kill",method= RequestMethod.POST)
    public JsonResult kill(@RequestBody @Valid KillParamDto dto){
        String orderNo= killCourseService.kill(dto);
        return JsonResult.success(orderNo);
    }



    /**
     * 从redis查询单个秒杀商品
     * @return
     */
    @RequestMapping(value="/online/one/{killId}/{activityId}",method= RequestMethod.GET)
    public JsonResult onlineOne(@PathVariable("killId")Long killId, @PathVariable("activityId")Long activityId){
        KillCourse killCourse =  killCourseService.onlineOne(activityId,killId);
        return JsonResult.success(killCourse);
    }



    /**
     * 查询所有上架的秒杀课程
     * @return
     */
    @RequestMapping(value="/online/all",method= RequestMethod.GET)
    public JsonResult onlineAll(){
       List<KillCourse> killCourses =  killCourseService.onlineAll();
        return JsonResult.success(killCourses);
    }




    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody KillCourse killCourse){


        killCourseService.addKillCourse(killCourse);
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        killCourseService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(killCourseService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(killCourseService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody KillCourseQuery query){
        Page<KillCourse> page = new Page<KillCourse>(query.getPage(),query.getRows());
        page = killCourseService.selectPage(page);
        return JsonResult.success(new PageList<KillCourse>(page.getTotal(),page.getRecords()));
    }
}
