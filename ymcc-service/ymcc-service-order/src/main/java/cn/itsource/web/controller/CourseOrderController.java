package cn.itsource.web.controller;

import cn.itsource.dto.OrderParamDto;
import cn.itsource.service.ICourseOrderService;
import cn.itsource.domain.CourseOrder;
import cn.itsource.query.CourseOrderQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/courseOrder")
public class CourseOrderController {

    @Autowired
    public ICourseOrderService courseOrderService;

    /**
     * 普通订单下单
     * @param dto
     * @return
     */
    @RequestMapping(value="/placeOrder",method= RequestMethod.POST)
    public JsonResult placeOrder(@RequestBody @Valid OrderParamDto dto){
        //下单成功需不需要返回值？ 返回什么？
        String orderNo = courseOrderService.placeOrder(dto);
        return JsonResult.success(orderNo);
    }

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseOrder courseOrder){
        if(courseOrder.getId()!=null){
            courseOrderService.updateById(courseOrder);
        }else{
            courseOrderService.insert(courseOrder);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        courseOrderService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(courseOrderService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(courseOrderService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseOrderQuery query){
        Page<CourseOrder> page = new Page<CourseOrder>(query.getPage(),query.getRows());
        page = courseOrderService.selectPage(page);
        return JsonResult.success(new PageList<CourseOrder>(page.getTotal(),page.getRecords()));
    }
}
