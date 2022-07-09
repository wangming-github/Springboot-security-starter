package com.zimug.courses.security.basic.auth.smscode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * 此时登录认证逻辑中的userDetailsService.loadUserByUsername的username就是手机号。
 * 我们在上文中已经支持了使用手机号去查询用户、角色、权限信息。
 */

@Slf4j
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 进行身份认证的逻辑
     *
     * @param authentication 就是我们传入的Token
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //利用UserDetailsService获取用户信息，拿到用户信息后重新组装一个已认证的Authentication

        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        log.info("==========> sms进行身份认证的逻辑获取userDetailsService中的用户信息");
        UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());  //根据手机号码拿到用户信息
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        log.info("==========> 返回 SmsCodeAuthenticationToken");
        return authenticationResult;
    }

    /**
     * AuthenticationManager挑选一个AuthenticationProvider
     * 来处理传入进来的Token就是根据supports方法来判断的
     *
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
