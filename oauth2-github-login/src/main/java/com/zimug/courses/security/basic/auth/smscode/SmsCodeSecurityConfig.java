package com.zimug.courses.security.basic.auth.smscode;

import com.zimug.courses.security.basic.auth.security.config.MyUserDetailsService;
import com.zimug.courses.security.basic.auth.security.handler.MyAuthenticationFailureHandler;
import com.zimug.courses.security.basic.auth.security.handler.MyAuthenticationSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 六、综合配置
 * 最后我们将以上实现进行组装，并将以上接口实现以配置的方式告知Spring Security。
 * 因为配置代码比较多，所以我们单独抽取一个关于短信验证码的配置类SmsCodeSecurityConfig，继承自SecurityConfigurerAdapter。
 *
 * @author maizi
 */
@Component
@Slf4j
public class SmsCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Resource
    private MyUserDetailsService myUserDetailsService;

    @Resource
    private SmsCodeValidateFilter smsCodeValidateFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //登录成功
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        //登录失败
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        // 新建验证码提供者
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(myUserDetailsService);

        http.authenticationProvider(smsCodeAuthenticationProvider)
                //在用户密码过滤器前面加入短信验证码校验过滤器
                .addFilterBefore(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //在用户密码过滤器后面加入短信验证码认证授权过滤器
                .addFilterBefore(smsCodeValidateFilter, SmsCodeAuthenticationFilter.class);

        log.info("==========> 综合配置 SmsCodeSecurityConfig完成");
    }
}