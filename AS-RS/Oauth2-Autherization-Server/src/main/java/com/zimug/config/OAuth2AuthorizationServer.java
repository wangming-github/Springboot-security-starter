package com.zimug.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MyUserDetailsService myUserDetailsService;
    @Resource
    private DataSource dataSource;//方式2:DefaultTokenServices#JdbcTokenStore
    @Resource
    private RedisConnectionFactory connectionFactory;//方式3:RedisTokenStore模式

    @Bean
    public TokenStore tokenStore() {
        //return new JdbcTokenStore(dataSource);//方式2:DefaultTokenServices#JdbcTokenStore
        return new RedisTokenStore(connectionFactory);//方式3:RedisTokenStore模式
    }


    //http://localhost:8084/oauth/authorize?client_id=client1&redirect_uri=http://localhost:8888/callback&response_type=code&scope=all
    //这个位置我们将Client客户端注册信息写死，后面章节我们会讲解动态实现
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("client1")// Client 账号
                .secret(passwordEncoder.encode("123456")) // Client  密码
                .redirectUris("http://localhost:8888/callback") // 配置回调地址，选填。
                .authorizedGrantTypes("authorization_code", "refresh_token") // 授权码模式
                .scopes("all")// 可授权的 Scope
                .accessTokenValiditySeconds(60 * 60 * 2)//token有效期设置2个小时
                .refreshTokenValiditySeconds(60 * 60 * 24);//Refresh_token:24个小时
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")//
                .checkTokenAccess("permitAll()").//
                checkTokenAccess("isAuthenticated()")//方式1:认证通过才可以访问
                .allowFormAuthenticationForClients();
    }


    /**
     * 为OAuth2AuthorizationServer配置类加入UserDetailsService,刷新令牌的时候需要用户信息
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints//
                .tokenStore(tokenStore())//方式2:DefaultTokenServices#JdbcTokenStore
                .userDetailsService(myUserDetailsService);
    }


}