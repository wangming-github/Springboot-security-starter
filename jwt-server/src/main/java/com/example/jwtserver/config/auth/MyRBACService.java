package com.example.jwtserver.config.auth;

import com.example.jwtserver.content.LogContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Component("rabcService")
public class MyRBACService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Resource
    private MyRBACServiceMapper myRBACServiceMapper;

    /**
     * 判断某用户是否具有该request资源的访问权限
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {


        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();

            List<String> urls = myRBACServiceMapper.findUrlsByUserName(username);
            boolean b = urls.stream().anyMatch(url -> antPathMatcher.match(url, request.getRequestURI()));
            log.info(LogContent.LOG_GREEN + "判断用户{}是否具有该request资源的访问权限:{}", username, b);
            return b;
        }
        log.info(LogContent.LOG_RED + "判断某用户是否具有该request资源的访问权限 非UserDetails类型");
        return false;
    }


}
