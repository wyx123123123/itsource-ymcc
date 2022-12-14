package cn.itsource.web.controller;

import cn.itsource.service.IUserAddressService;
import cn.itsource.domain.UserAddress;
import cn.itsource.query.UserAddressQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userAddress")
public class UserAddressController {

    @Autowired
    public IUserAddressService userAddressService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody UserAddress userAddress){
        if(userAddress.getId()!=null){
            userAddressService.updateById(userAddress);
        }else{
            userAddressService.insert(userAddress);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        userAddressService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(userAddressService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(userAddressService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody UserAddressQuery query){
        Page<UserAddress> page = new Page<UserAddress>(query.getPage(),query.getRows());
        page = userAddressService.selectPage(page);
        return JsonResult.success(new PageList<UserAddress>(page.getTotal(),page.getRecords()));
    }
}
