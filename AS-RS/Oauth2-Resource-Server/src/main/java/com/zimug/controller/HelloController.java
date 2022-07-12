package com.zimug.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author maizi
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class HelloController {


    @RequestMapping("/hello")
    public String hello(OAuth2Authentication authentication) {
        getExtraInfo(authentication);
        return "Hello Oauth2 Resource Server";
    }


    @Resource
    private TokenStore tokenStore;

    public void getExtraInfo(OAuth2Authentication auth) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
        Map<String, Object> map = accessToken.getAdditionalInformation();
        map.forEach((k, v) -> {
            System.out.println("====>传输信息:" + k + " =" + v);
        });
    }
}
