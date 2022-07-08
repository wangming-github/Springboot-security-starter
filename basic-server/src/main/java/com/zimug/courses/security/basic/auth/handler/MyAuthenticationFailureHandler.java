package com.zimug.courses.security.basic.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimug.commons.content.ResponseType;
import com.zimug.commons.exception.AjaxResponse;
import com.zimug.commons.exception.CustomException;
import com.zimug.commons.exception.CustomExceptionType;
import com.zimug.courses.security.basic.auth.config.UserDetailsServiceImpl;
import com.zimug.courses.security.basic.mapper.MyUserDetailsServiceMapper;
import com.zimug.courses.security.basic.model.SecurityUser;
import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Set;

/**
 * 2.7.多次登录失败账户锁定
 *
 * @author maizi
 * @see <a href="https://www.kancloud.cn/hanxt/springsecurity/2026500">
 */
@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${spring.security.loginType}")
    private String loginType;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    UserDetailsServiceImpl myUserDetailsService;

    @Resource
    MyUserDetailsServiceMapper myUserDetailsServiceMapper;

    /**
     * 规则定义：1分钟之内5次机会，就触发限流行为
     */
    Set<RequestLimitRule> rules = Collections.singleton(RequestLimitRule.of(Duration.ofMinutes(1), 5));
    RequestRateLimiter limiter = new InMemorySlidingWindowRequestRateLimiter(rules);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        //从request或request.getSession中获取登录用户名
        String username = request.getParameter("username");
        //默认提示信息
        String errorMsg = "用户名或者密码输入错误!";

        if (exception instanceof LockedException) {
            errorMsg = "您已经多次登陆失败，账户已被锁定，请稍后再试！";
        } else {
            //计数器加1，并判断该用户是否已经到了触发了锁定规则
            boolean reachLimit = limiter.overLimitWhenIncremented(username);
            if (reachLimit) { //如果触发了锁定规则，通过UserDetails告知Spring Security锁定账户
                SecurityUser user = (SecurityUser) myUserDetailsService.loadUserByUsername(username);
                user.setAccountNonLocked(false);
                //TODO 更新数据库后 每次多次登陆失败 除了等待时间过期还需重置状态
                myUserDetailsServiceMapper.updateAccountNonLockedByUsername(user);
            }
        }
        if (exception instanceof SessionAuthenticationException) {
            errorMsg = exception.getMessage();
        }

        if ("JSON".equalsIgnoreCase(loginType)) {
            response.setContentType(ResponseType.JSON);
            response.getWriter().write(objectMapper.writeValueAsString(AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, errorMsg))));
        } else {
            //跳转到登陆页面
            super.onAuthenticationFailure(request, response, exception);
        }

    }
}
