package cn.itsource.userdetails;

import cn.itsource.domain.Login;
import cn.itsource.domain.Permission;
import cn.itsource.mapper.PermissionMapper;
import cn.itsource.service.ILoginService;
import cn.itsource.util.AssertUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义的从数据查询登录信息
 */
@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private ILoginService loginService;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Wrapper<Login> wrapper = new EntityWrapper<>();
        wrapper.eq("username",username);
        Login login = loginService.selectOne(wrapper);
        AssertUtil.isNotNull(login,"用户不存在！！");
        List<Permission> permissions = permissionMapper.loadPermisisonsByLoginId(login.getId());
        List<GrantedAuthority> authorities = permissions.stream()
                .map(p->new SimpleGrantedAuthority(p.getSn())).collect(Collectors.toList());

        return new User(
                username,
                login.getPassword(),
                login.getEnabled(),login.getAccountNonExpired(),login.getCredentialsNonExpired(),login.getAccountNonLocked(),
                authorities
                );
    }
}
