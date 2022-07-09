package com.zimug.courses.security.basic.auth.security.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimug.commons.content.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * session超时或者踢人提示
 *
 * @author maizi
 */
@Component
@Slf4j
public class SessionExpiredServiceImpl implements SessionInformationExpiredStrategy {
    /**
     * 页面跳转的处理逻辑
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * jackson的JSON处理时象
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * session超时或者踢人 处理策略
     */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {

        log.info("======session超时或者踢人 处理策略======");

        //// 非前后端分离项目 进行页面跳转
        //HttpServletRequest request = event.getRequest();
        //HttpServletResponse response = event.getResponse();
        //redirectStrategy.sendRedirect(request, response, "/login");
        //log.info("已经被踢下线");


        //前后端分离项目 返回
        Map<String, Object> map = new HashMap<>();
        map.put("code", 403);
        map.put("msg", "您的登录已经超时或者已经在另一台机器登录，您被迫下线。");
        String json = objectMapper.writeValueAsString(map);
        //获取Response将数据返回
        HttpServletResponse response = event.getResponse();
        response.setContentType(ResponseType.JSON);
        response.getWriter().write(json);
    }
}