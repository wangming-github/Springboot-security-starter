package com.zimug.courses.security.basic.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimug.commons.content.ResponseType;
import com.zimug.commons.exception.AjaxResponse;
import com.zimug.commons.exception.CustomException;
import com.zimug.commons.exception.CustomExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 尚未配置
 *
 * @author maizi
 */
@Slf4j
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 除了登陆成功、登陆失败的结果处理，Spring Security还未我们提供了其他的结果处理类。
     * 比如用户未登录就访问系统资源，可以实现AuthenticationEntryPoint 接口进行响应处理，提示用户应该先去登录
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        //仿造上文使用response将响应信息写回
        log.info("尚未登录就访问系统资源....");
        log.info("=========执行无权访问逻辑================");
        response.setContentType(ResponseType.JSON);
        response.getWriter().write(objectMapper.writeValueAsString(AjaxResponse.error(new CustomException(CustomExceptionType.SC_FORBIDDEN))));
    }
}