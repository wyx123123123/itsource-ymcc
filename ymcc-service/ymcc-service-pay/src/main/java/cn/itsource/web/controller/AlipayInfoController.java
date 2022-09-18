package cn.itsource.web.controller;

import cn.itsource.service.IAlipayInfoService;
import cn.itsource.domain.AlipayInfo;
import cn.itsource.query.AlipayInfoQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alipayInfo")
public class AlipayInfoController {

    @Autowired
    public IAlipayInfoService alipayInfoService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody AlipayInfo alipayInfo){
        if(alipayInfo.getId()!=null){
            alipayInfoService.updateById(alipayInfo);
        }else{
            alipayInfoService.insert(alipayInfo);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        alipayInfoService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(alipayInfoService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(alipayInfoService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody AlipayInfoQuery query){
        Page<AlipayInfo> page = new Page<AlipayInfo>(query.getPage(),query.getRows());
        page = alipayInfoService.selectPage(page);
        return JsonResult.success(new PageList<AlipayInfo>(page.getTotal(),page.getRecords()));
    }
}
