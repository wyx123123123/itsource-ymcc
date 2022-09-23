package cn.itsource.web.controller;

import cn.itsource.service.ILoginService;
import cn.itsource.domain.Login;
import cn.itsource.query.LoginQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    public ILoginService loginService;

    //登录成功后重定向地址
    @RequestMapping("/loginSuccess")
    @ResponseBody
    public String loginSuccess(){
        return "登录成功";
    }

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody Login login){
        if(login.getId()!=null){
            loginService.updateById(login);
        }else{
            // mybaits-plus自动帮我们把主键返回到传递进去的对象的主键中
            loginService.insert(login);
        }
        return JsonResult.success(login.getId());
    }

    /**
    * 删除对象
    */
    @PreAuthorize("hasAuthority('delete:id')")
    @RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        loginService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @PreAuthorize("hasAuthority('get:id')")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(loginService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @PreAuthorize("hasAuthority('login:list')")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(loginService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody LoginQuery query){
        Page<Login> page = new Page<Login>(query.getPage(),query.getRows());
        page = loginService.selectPage(page);
        return JsonResult.success(new PageList<Login>(page.getTotal(),page.getRecords()));
    }
}
