package com.zimug.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class MyJwtTokenCofnig {

    //@Bean
    //public TokenStore tokenStore() {
    //    //JwtTokenStore是一种特殊的TokenStore，它不将令牌信息存储到内存或者数据库。而是让令牌携带状态信息，这是JWT令牌的特性。
    //    return new JwtTokenStore(jwtAccessTokenConverter());
    //}

    //用于JWT令牌生成，需要设置用于签名解签名的secret密钥
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        //JwtAccessTokenConverter用于生成JWT令牌，所以需要设置用于签名解签名的secret密钥
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("用于签名解签名的secret密钥");
        return accessTokenConverter;
    }

    @Bean
    @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
    public TokenEnhancer jwtTokenEnhancer() {
        //TokenEnhancer用来向JWT令牌中加入附加信息，也就是JWT令牌中的payload部分（相关理论内容回看3.1小节）
        return new MyJwtTokenEnhancer();
    }

}