package com.zimug.courses.security.basic.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimug.commons.content.ResponseType;
import com.zimug.commons.exception.AjaxResponse;
import com.zimug.commons.exception.CustomException;
import com.zimug.commons.exception.CustomExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 无权访问处理
 *
 * @author maizi
 */
@Component
@Slf4j
public class MyAccessDeniedHandler implements AccessDeniedHandler {


    @Value("${spring.security.loginType}")
    private String loginType;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        log.info("=========执行无权访问逻辑================");
        response.setContentType(ResponseType.JSON);
        response.getWriter().write(objectMapper.writeValueAsString(AjaxResponse.error(new CustomException(CustomExceptionType.SC_FORBIDDEN))));
    }
}
