package com.zimug.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {


    /**
     * 配置资源服务器,对任何“/api/**”接口的访问，都必须经过OAuth2认证服务器认证。
     * ● @Configuration表明该类是一个配置类，保证@EnableResourceServer被Spring Boot扫描到
     * ● @EnableResourceServer说明该配置类，是针对OAuth2的ResourceServer资源服务器的配置。
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //
                .anyRequest().authenticated()
                //
                .and().requestMatchers().antMatchers("/api/**");
    }

}