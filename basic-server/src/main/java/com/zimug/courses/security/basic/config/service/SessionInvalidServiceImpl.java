package com.zimug.courses.security.basic.config.service;


import com.zimug.commons.content.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * session超时或者踢人提示
 *
 * @author maizi
 */
@Slf4j
public class SessionInvalidServiceImpl implements InvalidSessionStrategy {
    @Override
    public void onInvalidSessionDetected(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        log.info("session 无效");
        httpServletResponse.setContentType(ResponseType.JSON);
        httpServletResponse.getWriter().write("session 无效");
    }
}