package com.example.jwtserver.config.auth;

import com.example.jwtserver.content.LogContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    @Resource
    private MyUserDetailsServiceMapper myUserDetailsServiceMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(LogContent.LOG_GREEN + "JWT认证流程 3.谓用UserDetaiIsService");
        log.info(LogContent.LOG_GREEN + "JWT认证流程 4.根据用户名加载用户信息");

        //加载基础用户信息
        MyUserDetails myUserDetails = myUserDetailsServiceMapper.findByUserName(username);

        //加载用户角色列表
        List<String> roleCodes = myUserDetailsServiceMapper.findRoleByUserName(username);


        //通过用户角色列表加载用户的资源权限列表
        List<String> authorties = myUserDetailsServiceMapper.findAuthorityByRoleCodes(roleCodes);

        //角色是一个特殊的权限，ROLE_前缀
        roleCodes = roleCodes.stream().map(rc -> "ROLE_" + rc).collect(Collectors.toList());

        authorties.addAll(roleCodes);

        myUserDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", authorties)));

        log.info(LogContent.LOG_GREEN + "JWT认证流程 5.进行登录认证");
        log.info(LogContent.LOG_GREEN + "JWT认证流程 6.认证结果");
        return myUserDetails;
    }
}
