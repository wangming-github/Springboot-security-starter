package com.zimug.courses.security.basic.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author maizi
 */

@Component
@Slf4j
public class LogoutFilter implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException,
            ServletException {
        log.info("=========执行退出成功逻辑================");

        //必须这里指定不能简易配置了
        httpServletResponse.sendRedirect("/login.html");
    }
}
