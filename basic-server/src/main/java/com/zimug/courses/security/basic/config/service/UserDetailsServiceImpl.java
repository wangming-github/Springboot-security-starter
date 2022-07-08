package com.zimug.courses.security.basic.config.service;

import com.zimug.courses.security.basic.mapper.MyUserDetailsServiceMapper;
import com.zimug.courses.security.basic.model.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * UserDetailsService
 *
 * @author maizi
 */
@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {


    @Resource
    private MyUserDetailsServiceMapper mapper;

    /**
     * @param username 用户唯一标示，登录时的用户名内容
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SecurityUser securityUser = mapper.findUserByUsername(username);
        if (securityUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }


        //用户的角色列表
        List<String> roleCodes = mapper.findRoleCodeByUsername(username);
        //根据角色列表加载当前用户具有的权限
        List<String> authorities = mapper.findAuthorityByUsername(roleCodes);
        //所有权限
        authorities.addAll(roleCodes.stream().map(x -> "ROLE_" + x).collect(Collectors.toList()));
        //String角色列表转换为Authorities所需
        securityUser.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", authorities)));

        return securityUser;
    }
}
