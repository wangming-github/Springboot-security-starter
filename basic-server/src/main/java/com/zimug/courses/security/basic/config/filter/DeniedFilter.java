package com.zimug.courses.security.basic.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 无权访问处理
 *
 * @author maizi
 */
@Component
@Slf4j
public class DeniedFilter implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        log.info("=========执行无权访问逻辑================");


        Map<String, Object> map = new HashMap<>();
        map.put("code", HttpServletResponse.SC_FORBIDDEN);
        map.put("msg", "无权访问...");
        String s = objectMapper.writeValueAsString(map);

        //获取Response将数据返回
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(s);
    }
}
