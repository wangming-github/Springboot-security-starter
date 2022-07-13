package com.zimug.courses.security.basic.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimug.commons.content.ResponseType;
import com.zimug.commons.exception.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 记录上一次请求地址
 * -books.html -> login.html 登陆成功 ->books.html
 *
 * @author maizi
 */
@Slf4j
//@Component("MyAuthenticationSuccessHandler") 尚未配置这种方式
public class MySaReAuthenticationSuHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${spring.security.loginType}")
    private String loginType;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        log.info("==========> 登录成功逻辑");

        if ("JSON".equalsIgnoreCase(loginType)) {//如果配置了JSON格式，返回如下信息
            authentication.getPrincipal();
            response.setContentType(ResponseType.JSON);//指定响应类型JSON
            response.getWriter().write(objectMapper.writeValueAsString(AjaxResponse.success()));
            log.info("==========> 登录成功逻辑  配置了JSON返回...");
        } else {//否则执行父类默认的方式，跳转上一次请求地址
            super.onAuthenticationSuccess(request, response, authentication);
        }
        log.info("==========> 登录成功逻辑完成");
    }
}
