package com.zimug.config;

import com.zimug.config.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maizi
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {


    //--------------------------公共配置--------------------------------------

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MyUserDetailsService myUserDetailsService;
    @Resource
    private AuthenticationManager authenticationManager;


    //--------------------------可选：JDBC--------------------------------------

    //@Resource
    //private DataSource dataSource;
    //
    //@Bean
    //public TokenStore tokenStore() {
    //    return new JdbcTokenStore(dataSource);
    //}


    //--------------------------可选：Redis--------------------------------------

    //@Resource
    //private RedisConnectionFactory connectionFactory;
    //
    //
    //@Bean
    //public TokenStore tokenStore() {
    //    return new RedisTokenStore(connectionFactory);
    //}

    //--------------------------可选：Jwt--------------------------------------


    @Resource
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Resource
    private TokenEnhancer jwtTokenEnhancer;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    //--------------------------公共配置--------------------------------------

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")//
                .checkTokenAccess("isAuthenticated()")//方式1:认证通过才可以访问
                .allowFormAuthenticationForClients();
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())//tokenStore
                .authenticationManager(authenticationManager)//
                .userDetailsService(myUserDetailsService);

        //整合JWT
        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancerList = new ArrayList<>();
            enhancerList.add(jwtTokenEnhancer);
            enhancerList.add(jwtAccessTokenConverter);
            tokenEnhancerChain.setTokenEnhancers(enhancerList);
            //jwt
            endpoints.tokenEnhancer(tokenEnhancerChain).accessTokenConverter(jwtAccessTokenConverter);
        }
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()//
                .withClient("client1").secret(passwordEncoder.encode("123456")) // Client 账号  密码
                .redirectUris("http://localhost:8888/callback") // 配置回调地址，选填。
                .authorizedGrantTypes("authorization_code", "password", "implicit", "client_credentials", "refresh_token") // 授权码模式
                .scopes("all")// 可授权的 Scope
                .accessTokenValiditySeconds(60 * 60 * 2)//token有效期设置2个小时
                .refreshTokenValiditySeconds(60 * 60 * 24);//Refresh_token:24个小时
    }


}