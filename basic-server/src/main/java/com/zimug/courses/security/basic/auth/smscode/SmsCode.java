package com.zimug.courses.security.basic.auth.smscode;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 使用SmsCode封装短信验证码的谜底，用于后续登录过程中进行校验。
 *
 * @author maizi
 */
@Slf4j
public class SmsCode {
    private String code;  //短信验证码
    private LocalDateTime expireTime; //验证码的过期时间
    private String mobile; //发送手机号

    public SmsCode(String code, int expireAfterSeconds, String mobile) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterSeconds);
        this.mobile = mobile;
    }

    public boolean isExpired() {
        log.info("==========> 检查验证码是否过期..");
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public String getMobile() {
        return mobile;
    }

}