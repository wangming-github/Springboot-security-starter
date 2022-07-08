package com.zimug.courses.security.basic.config.service;

import com.zimug.courses.security.basic.model.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 检查本次请求是否有权访问
 * 配置到
 *
 * @author maizi
 */

@Component("UrlPermissionService")
@Slf4j
public class UrlPermissionCheckService {


    /**
     * 检查本次请求是否有权访问
     *
     * @param request        HttpServletRequest
     * @param authentication 上下文UserDetails
     * @return false、true
     */
    public boolean checkUrlPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();//获取当前登录用户信息
        if (principal instanceof UserDetails) { //类型判断
            String requestUri = request.getRequestURI(); //获取请求路径
            //本次要访问的资源
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(requestUri);//封装成SimpleGrantedAuthority
            SecurityUser user = (SecurityUser) principal;
            log.info("===URL：{}", requestUri);
            //当前用户所有资源getAuthorities
            return user.getAuthorities().contains(simpleGrantedAuthority);//判断登录用户权限中是否包含登录请求
        }
        return false;
    }
}
