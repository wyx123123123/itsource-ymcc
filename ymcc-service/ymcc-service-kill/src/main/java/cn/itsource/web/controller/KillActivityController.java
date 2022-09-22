package cn.itsource.web.controller;

import cn.itsource.service.IKillActivityService;
import cn.itsource.domain.KillActivity;
import cn.itsource.query.KillActivityQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/killActivity")
public class KillActivityController {

    @Autowired
    public IKillActivityService killActivityService;
    private SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");

    /**
     * 发布活动
     * @param activityId
     * @return
     */
    @RequestMapping(value="/publish/{activityId}",method= RequestMethod.POST)
    public JsonResult publish(@PathVariable("activityId") Long activityId){
        killActivityService.publish(activityId);
        return JsonResult.success();
    }


    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody KillActivity killActivity){
        String timeStr = format.format(killActivity.getStartTime());
        killActivity.setTimeStr(timeStr);
        killActivityService.insert(killActivity);
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        killActivityService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(killActivityService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(killActivityService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody KillActivityQuery query){
        Page<KillActivity> page = new Page<KillActivity>(query.getPage(),query.getRows());
        page = killActivityService.selectPage(page);
        return JsonResult.success(new PageList<KillActivity>(page.getTotal(),page.getRecords()));
    }
}
