package com.zimug.courses.security.basic.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CaptchaImageVO {


    private String code;  //验证码文字
    private LocalDateTime expireTime; //验证码失效时间

    public CaptchaImageVO(String code, int expireAfterSeconds) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterSeconds);
    }

    //验证码是否失效
    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }
}