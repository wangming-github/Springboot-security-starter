package com.zimug.courses.security.basic.controller;

import com.zimug.commons.exception.AjaxResponse;
import com.zimug.commons.exception.CustomException;
import com.zimug.commons.exception.CustomExceptionType;
import com.zimug.courses.security.basic.auth.smscode.SmsCode;
import com.zimug.courses.security.basic.mapper.MyUserDetailsServiceMapper;
import com.zimug.courses.security.basic.model.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 到这里，我们可以讲一下整体的短信验证登录流程，如上面的时序图。
 * <p>
 * 首先用户发起“获取短信验证码”请求，SmsCodeController中调用短信服务商接口发送短信，并将短信发送的“谜底”保存在session中。
 * 当用户发起登录请求，首先要经过SmsCodeValidateFilter对谜底和用户输入进行比对，比对失败则返回短信验证码校验失败
 * 当短信验证码校验成功，继续执行过滤器链中的SmsCodeAuthenticationFilter对用户进行认证授权。
 *
 * @author maizi
 */
@Slf4j
@RestController
public class SmsController {

    @Resource
    MyUserDetailsServiceMapper myUserDetailsServiceMapper;


    /**
     * 接收用户的获取验证码请求。
     * 注意：一定要为“/smscode”访问路径配置为permitAll访问权限。
     * 获取短信验证码
     */

    @RequestMapping(value = "/smscode", method = RequestMethod.GET)
    public AjaxResponse sms(@RequestParam String mobile, HttpSession session) throws IOException {

        SecurityUser myUserDetails = myUserDetailsServiceMapper.findUserByUsername(mobile);
        if (myUserDetails == null) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "您输入的手机号不是系统注册用户"));
        }

        SmsCode smsCode = new SmsCode(RandomStringUtils.randomNumeric(4), 60, mobile);
        //TODO 此处调用验证码发送服务接口
        log.info("==========> 模拟验证码[{}] 发送到手机[{}] ", smsCode.getCode(), mobile);
        session.setAttribute("sms_key", smsCode);
        return AjaxResponse.success("短信息已经发送到您的手机: " + smsCode.getCode());
    }
}