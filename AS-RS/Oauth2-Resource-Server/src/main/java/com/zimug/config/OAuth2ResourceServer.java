package com.zimug.config;

import com.mysql.cj.log.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableResourceServer
public class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {

    @Resource
    private DataSource dataSource;

    @Resource
    private RedisConnectionFactory redisConnectionFactory;


    /**
     * 配置资源服务器,对任何“/api/**”接口的访问，都必须经过OAuth2认证服务器认证。
     * ● @Configuration表明该类是一个配置类，保证@EnableResourceServer被Spring Boot扫描到
     * ● @EnableResourceServer说明该配置类，是针对OAuth2的ResourceServer资源服务器的配置。
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//
                .anyRequest().authenticated()//
                .and().requestMatchers().antMatchers("/api/**");
    }


    /**
     * 方式1:RemoteTokenServices
     * 用户=======>资源服务器=======>认证服务器
     */
    @Bean
    public RemoteTokenServices tokenServices() {
        log.info("当前认证模式:方式1:RemoteTokenServices");
        final RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl("http://localhost:8001/oauth/check_token");
        tokenService.setClientId("client1");
        tokenService.setClientSecret("123456");
        return tokenService;
    }

    /**
     * 方式2:DefaultTokenServices
     * 用户=======>资源服务器=======>DB/Redis ↓
     * 用户=======>认证服务器=======>DB/Redis ↑
     */
    @Primary
    @Bean
    public DefaultTokenServices tokenStores() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        //defaultTokenServices.setTokenStore(new JdbcTokenStore(dataSource));//方式1#JdbcTokenStore
        defaultTokenServices.setTokenStore(new RedisTokenStore(redisConnectionFactory));//方式2#RedisTokenStore
        return defaultTokenServices;
    }


    /**
     * 使用那种校验方式
     * 在对应 tokenStores()方法上，使用@Primary注解将该Bean为首选者。
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenServices(tokenStores());
    }


}