package cn.itsource.web.controller;

import cn.itsource.dto.PhoneRegisterDto;
import cn.itsource.service.IUserService;
import cn.itsource.domain.User;
import cn.itsource.query.UserQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public IUserService userService;

    /*
     * @Description: 手机号注册
     * @Author: Director
     * @Date: 2022/8/28 16:45
     * @param phoneRegisterDto: 手机号注册临时对象
     * @return: cn.itsource.result.JsonResult
     **/
    @RequestMapping(value="/register",method= RequestMethod.POST)
    public JsonResult register(@RequestBody PhoneRegisterDto phoneRegisterDto){
        userService.register(phoneRegisterDto);
        return JsonResult.success();
    }

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody User user){
        if(user.getId()!=null){
            userService.updateById(user);
        }else{
            userService.insert(user);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        userService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(userService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(userService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody UserQuery query){
        Page<User> page = new Page<User>(query.getPage(),query.getRows());
        page = userService.selectPage(page);
        return JsonResult.success(new PageList<User>(page.getTotal(),page.getRecords()));
    }
}
