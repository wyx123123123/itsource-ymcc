package cn.itsource.web.controller;

import cn.itsource.service.IMessageStationService;
import cn.itsource.domain.MessageStation;
import cn.itsource.query.MessageStationQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messageStation")
public class MessageStationController {

    @Autowired
    public IMessageStationService messageStationService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody MessageStation messageStation){
        if(messageStation.getId()!=null){
            messageStationService.updateById(messageStation);
        }else{
            messageStationService.insert(messageStation);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        messageStationService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(messageStationService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(messageStationService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody MessageStationQuery query){
        Page<MessageStation> page = new Page<MessageStation>(query.getPage(),query.getRows());
        page = messageStationService.selectPage(page);
        return JsonResult.success(new PageList<MessageStation>(page.getTotal(),page.getRecords()));
    }
}
