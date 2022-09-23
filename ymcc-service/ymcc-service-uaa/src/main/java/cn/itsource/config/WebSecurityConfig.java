package cn.itsource.config;

import cn.itsource.domain.Permission;
import cn.itsource.service.IPermissionService;
import cn.itsource.userdetails.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

@Configuration
@EnableWebSecurity// 开启SpringSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled= true)  //开启方法授权
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private IPermissionService permissionService;

     //提供用户信息，这里没有从数据库查询用户信息，在内存中模拟

    /**
     * 项目在启动的时候，装载InMemoryUserDetailsManager,基于内存存储了一个登录人信息
     * 前端输入账号密码，点击登录的时候，SpringSecurity会通过拦截器然后使用内存中的登录人信息  + PasswordEncoder来比对密码
     * 比对正确就认证成功了，会将认证成功之后的信息（登录人信息和权限）存储到SecurityContext中
     */



    /*
      我们使用OAuth2的密码模式来获取token，而获取token必须要先登录，
      OAuth2没有认证的功能，OAuth2会使用AuthenticationManager（认证管理器）去使用Security
      完成认证【Security使用UserDetailService加载数据库中的账密码和权限，使用PasswordEncoder 进行密码比对】,
      如果比对成功即登录成功，SecurityContext中就有登录人信息+权限信息了
     */
    //配置认证管理器，授权模式为“password”时会用到
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
  

    //密码编码器：不加密
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    //授权规则配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*List<Permission> permissions = permissionService.selectList(null);
        for (Permission permission : permissions) {
            http.authorizeRequests().antMatchers(permission.getResource()).hasAuthority(permission.getSn());
        }*/
        http.authorizeRequests()                                //授权配置
                .antMatchers("/login").permitAll()  //登录路径放行
                .anyRequest().authenticated()                   //其他路径都要认证之后才能访问
                .and().formLogin()                              //允许表单登录
                .successForwardUrl("/login/loginSuccess")             // 设置登陆成功页
                .and().logout().permitAll()                    //登出路径放行 /logout
                .and().csrf().disable();                        //关闭跨域伪造检查
    }
}