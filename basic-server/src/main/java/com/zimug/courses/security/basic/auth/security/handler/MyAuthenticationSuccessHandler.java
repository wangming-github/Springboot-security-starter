package com.zimug.courses.security.basic.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimug.commons.content.ResponseType;
import com.zimug.commons.exception.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 记录上一次请求地址
 * -books.html -> login.html 登陆成功 ->books.html
 *
 * @author maizi
 */
@Slf4j
@Component("MyAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${spring.security.loginType}")
    private String loginType;
    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException,
            ServletException {


        //前后端分离 JSON交互
        if (loginType.equalsIgnoreCase("JSON")) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(AjaxResponse.success()));
            log.info("==========> 登录成功逻辑 由前端处理转发");
        } else {
            //后端实现跳转 TODO 有问题
            httpServletResponse.setContentType("text/html;charset=utf-8");
            log.info("==========> 登录成功逻辑 由后端处理转发");
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            if (roles.contains("ROLE_admin")) {
                httpServletResponse.sendRedirect("/index_alt");
            } else {
                httpServletResponse.sendRedirect("/index_alt");
            }
        }
    }
}

