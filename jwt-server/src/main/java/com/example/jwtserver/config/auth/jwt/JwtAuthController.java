package com.example.jwtserver.config.auth.jwt;

import com.example.jwtserver.config.exception.AjaxResponse;
import com.example.jwtserver.config.exception.CustomException;
import com.example.jwtserver.config.exception.CustomExceptionType;
import com.example.jwtserver.content.LogContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 三、开发登录接口（获取Token的接口）
 */
@RestController
@Slf4j
public class JwtAuthController {


    @Resource
    JwtAuthService jwtAuthService;

    /**
     * "/authentication"接口用于登录验证，并且生成JWT返回给客户端
     */
    @RequestMapping(value = "/authentication")
    public AjaxResponse login(@RequestBody Map<String, String> map) {
        log.info(LogContent.LOG_GREEN + "JWT认证流程 1.发起登录请求用户名+密码");
        String username = map.get("username");
        String password = map.get("password");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名或者密码不能为空"));
        }
        try {
            AjaxResponse success = AjaxResponse.success(jwtAuthService.login(username, password));
            log.info(LogContent.LOG_GREEN + "JWT认证流程 9.返回JWT令牌");
            log.info(LogContent.LOG_GREEN + "JWT认证流程 10.认证成功+JWT令牌");
            return success;
        } catch (CustomException e) {
            return AjaxResponse.error(e);
        }
    }


    /**
     * "/refreshtoken"接口用于刷新JWT，更新JWT令牌的有效期
     */
    @RequestMapping(value = "/refreshtoken")
    public AjaxResponse refresh(@RequestHeader("${jwt.header}") String token) {
        return AjaxResponse.success(jwtAuthService.refreshToken(token));
    }

}
