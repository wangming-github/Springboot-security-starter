package com.zimug.config;

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
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Slf4j
@Configuration//@Configuration表明该类是一个配置类，保证@EnableResourceServer被Spring Boot扫描到
@EnableResourceServer//@EnableResourceServer说明该配置类，是针对OAuth2的ResourceServer资源服务器的配置。
public class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {


    /*
     * ---------------------------可选：Remote-------------------------------------
     * RemoteTokenServices方式
     * 用户=======>资源服务器=======>认证服务器
     */

    //@Bean
    //public RemoteTokenServices remoteTokenServices() {
    //    log.info("当前认证模式:方式1:RemoteTokenServices");
    //    final RemoteTokenServices tokenService = new RemoteTokenServices();
    //    tokenService.setCheckTokenEndpointUrl("http://localhost:8001/oauth/check_token");
    //    tokenService.setClientId("client1");
    //    tokenService.setClientSecret("123456");
    //    return tokenService;
    //}


    //---------------------------可选：JDBC-------------------------------------

    //@Resource
    //private DataSource dataSource; //JDBC
    //
    //@Bean
    //public TokenStore tokenStore() {
    //    return new JdbcTokenStore(dataSource);
    //}

    //----------------------------可选：Redis------------------------------------

    //@Resource
    //private RedisConnectionFactory redisConnectionFactory;//Redis
    //
    //@Bean
    //public TokenStore tokenStore() {
    //    return new RedisTokenStore(redisConnectionFactory);
    //}


    //--------------------------Jwt--------------------------------------

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("用于签名解签名的secret密钥");
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }


    //--------------------------公共配置--------------------------------------

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenServices(tokenServices());
    }


    /**
     * 配置资源服务器
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//
                .anyRequest().authenticated()//
                //对任何“/api/**”接口的访问，都必须经过OAuth2认证服务器认证。
                .and().requestMatchers().antMatchers("/api/**");
    }


}