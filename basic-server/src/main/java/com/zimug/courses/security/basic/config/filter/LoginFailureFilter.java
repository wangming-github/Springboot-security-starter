package com.zimug.courses.security.basic.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimug.commons.exception.AjaxResponse;
import com.zimug.commons.exception.CustomException;
import com.zimug.commons.exception.CustomExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 登录失败 跳转到登录页面
 *
 * @author maizi
 */

@Slf4j
@Component
public class LoginFailureFilter extends SimpleUrlAuthenticationFailureHandler {


    @Value("${spring.security.loginType}")
    private String loginType;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("=========登录失败逻辑=========");
        if ("JSON".equalsIgnoreCase(loginType)) {//如果配置了JSON格式，返回如下信息
            log.info("> 配置了JSON返回....");
            response.setContentType("application/json;charset=UTF-8");//指定响应类型JSON
            response.getWriter().write(objectMapper.writeValueAsString(AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR))));
        } else {
            //否则执行父类默认的方式
            response.setContentType("text/html; charset=UTF-8");
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
