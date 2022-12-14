package cn.itsource.web.controller;

import cn.itsource.service.IUserAccountService;
import cn.itsource.domain.UserAccount;
import cn.itsource.query.UserAccountQuery;
import cn.itsource.result.JsonResult;
import cn.itsource.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userAccount")
public class UserAccountController {

    @Autowired
    public IUserAccountService userAccountService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody UserAccount userAccount){
        if(userAccount.getId()!=null){
            userAccountService.updateById(userAccount);
        }else{
            userAccountService.insert(userAccount);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id){
        userAccountService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id")Long id){
        return JsonResult.success(userAccountService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(userAccountService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JsonResult page(@RequestBody UserAccountQuery query){
        Page<UserAccount> page = new Page<UserAccount>(query.getPage(),query.getRows());
        page = userAccountService.selectPage(page);
        return JsonResult.success(new PageList<UserAccount>(page.getTotal(),page.getRecords()));
    }
}
