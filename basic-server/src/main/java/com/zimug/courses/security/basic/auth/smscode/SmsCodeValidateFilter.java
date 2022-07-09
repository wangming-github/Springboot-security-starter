package com.zimug.courses.security.basic.auth.smscode;


import com.zimug.courses.security.basic.auth.security.handler.MyAuthenticationFailureHandler;
import com.zimug.courses.security.basic.mapper.MyUserDetailsServiceMapper;
import com.zimug.courses.security.basic.model.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * 四、短信验证码校验过滤器
 * 短信验证码的校验过滤器，和图片验证码的验证实现原理是一致的。都是通过继承OncePerRequestFilter实现一个Spring环境下的过滤器。其核心校验规则如下：
 * <p>
 * 用户登录时手机号不能为空
 * 用户登录时短信验证码不能为空
 * 用户登陆时在session中必须存在对应的校验谜底（获取验证码时存放的）
 * 用户登录时输入的短信验证码必须和“谜底”中的验证码一致
 * 用户登录时输入的手机号必须和“谜底”中保存的手机号一致
 * 用户登录时输入的手机号必须是系统注册用户的手机号，并且唯一
 *
 * @author maizi
 */
@Component
@Slf4j
public class SmsCodeValidateFilter extends OncePerRequestFilter {

    @Resource
    MyUserDetailsServiceMapper myUserDetailsServiceMapper;

    @Resource
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("==========>  RequestURI：{}", request.getRequestURI());
        if ("/smslogin".equals(request.getRequestURI()) && "post".equalsIgnoreCase(request.getMethod())) {
            log.info("==========> 当前URL为[/smslogin] 开始进行短信方式验证...");
            try {
                validate(new ServletWebRequest(request));
            } catch (AuthenticationException e) {
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        HttpSession session = request.getRequest().getSession();
        SmsCode codeInSession = (SmsCode) session.getAttribute("sms_key");
        String codeInRequest = request.getParameter("smsCode");
        String mobileInRequest = request.getParameter("mobile");


        if (StringUtils.isEmpty(mobileInRequest)) {
            throw new SessionAuthenticationException("手机号码不能为空！");
        }
        if (StringUtils.isEmpty(codeInRequest)) {
            throw new SessionAuthenticationException("短信验证码不能为空！");
        }
        if (Objects.isNull(codeInSession)) {
            throw new SessionAuthenticationException("短信验证码不存在！");
        }
        if (codeInSession.isExpired()) {
            session.removeAttribute("sms_key");
            throw new SessionAuthenticationException("短信验证码已过期！");
        }
        if (!codeInSession.getCode().equals(codeInRequest)) {
            throw new SessionAuthenticationException("短信验证码不正确！");
        }

        if (!codeInSession.getMobile().equals(mobileInRequest)) {
            throw new SessionAuthenticationException("短信发送目标与该手机号不一致！");
        }

        SecurityUser myUserDetails = myUserDetailsServiceMapper.findUserByUsername(mobileInRequest);
        if (Objects.isNull(myUserDetails)) {
            throw new SessionAuthenticationException("您输入的手机号不是系统的注册用户");
        }
        log.info("==========> 短信方式验证通过 移除session验证码...");
        session.removeAttribute("sms_key");
    }
}