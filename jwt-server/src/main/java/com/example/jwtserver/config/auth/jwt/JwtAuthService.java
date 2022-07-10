package com.example.jwtserver.config.auth.jwt;

import com.example.jwtserver.config.exception.CustomException;
import com.example.jwtserver.config.exception.CustomExceptionType;
import com.example.jwtserver.content.LogContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 核心的token业务逻辑
 *
 * @author maizi
 */
@Service
@Slf4j
public class JwtAuthService {

    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    UserDetailsService userDetailsService;

    @Resource
    JwtTokenUtil jwtTokenUtil;

    /**
     * 登录认证换取JWT令牌
     * login方法中首先使用用户名、密码进行登录验证。如果验证失败抛出AuthenticationException 异常。
     * 如果验证成功，程序继续向下走，生成JWT响应给前端
     *
     * @return JWT
     */
    public String login(String username, String password) throws CustomException {
        try {
            //使用用户名密码进行登录验证
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
            log.info(LogContent.LOG_GREEN + "JWT认证流程 2.调月AuthenticationManager");


            Authentication authentication = authenticationManager.authenticate(upToken);
            log.info(LogContent.LOG_GREEN + "JWT认证流程 5.进行登最认证");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            log.info(LogContent.LOG_GREEN + "JWT认证流程  7.如采认证失败，结果响应");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名或者密码不正确");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        log.info(LogContent.LOG_GREEN + "JWT认证流程 8.如果认证成功，调用JwtTokenUtil生成JWT令牌");
        return jwtTokenUtil.generateToken(userDetails);
    }


    /**
     * refreshToken方法只有在JWT token没有过期的情况下才能刷新，过期了就不能刷新了。需要重新登录。
     *
     * @param oldToken
     * @return
     */
    public String refreshToken(String oldToken) {
        if (!jwtTokenUtil.isTokenExpired(oldToken)) {
            return jwtTokenUtil.refreshToken(oldToken);
        }
        return null;
    }


}
