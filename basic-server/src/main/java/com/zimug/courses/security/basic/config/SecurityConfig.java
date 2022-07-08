package com.zimug.courses.security.basic.config;


import com.zimug.courses.security.basic.config.filter.DeniedFilter;
import com.zimug.courses.security.basic.config.filter.LoginSuccessFilter;
import com.zimug.courses.security.basic.config.filter.LogoutFilter;
import com.zimug.courses.security.basic.config.service.MyAuthenticationFailureHandler;
import com.zimug.courses.security.basic.config.service.SessionExpiredServiceImpl;
import com.zimug.courses.security.basic.config.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author maizi
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//使用注解的方式进行鉴权开关
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Resource
    private MyAuthenticationFailureHandler loginFailureFilter;
    @Resource
    private LoginSuccessFilter loginSuccessFilter;
    @Resource
    private LogoutFilter logoutFilter;
    @Autowired
    private UserDetailsServiceImpl myUserDetailsService;
    @Autowired
    private DeniedFilter deniedFilter;
    @Autowired
    private SessionExpiredServiceImpl sessionExpiredServiceImpl;


    /**
     * 方式1. httpBasic认证模式
     */
    //@Override
    //protected void configure(HttpSecurity http) throws Exception {
    //    //1.SpringSecurity认证模式:httpBasic
    //    http.httpBasic();
    //    //2.所有请求都需要登录认证才能访问
    //    http.authorizeRequests().anyRequest().authenticated();
    //}


    /**
     * 方式2. formLogin认证模式使用过滤器进行验证
     *
     * @see org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //1.登录认证逻辑-登录UL、如何接收登录参数、登陆成功后逻辑（静态）
        http.formLogin()//设置认证模式:formLogin
                .loginPage("/login.html")//未登录时返回页面
                .usernameParameter("username")//页面用户名
                .passwordParameter("password")//页面密码
                .loginProcessingUrl("/login") //登录页面Action

                //方式1.使用 默认快捷方式
                //.defaultSuccessUrl("/")//登录成功跳转 index.html（"/"默认等于resource/templates/index.html）
                //.failureUrl("/login.html")//登录失败

                //方式2..使用 自定义的 LoginFailureFilter, LoginSuccessFilter 处理登录成功失败
                .successHandler(loginSuccessFilter).failureHandler(loginFailureFilter);


        //2.资源访问控制-决定什么用户、什么角色可以访问什么资源（动态-数据库）
        http.authorizeRequests()//authorizeRequests里按照谁先匹配，就匹配谁的逻辑执行不是无序的
                .antMatchers("/login", "/login.html", "/error").permitAll()//登录无需认证

                //方式1 使用静态配置
                //角色是一种特殊的权限 hasAnyAuthority ("ROLE_user","ROLE_admin") <==等价于==> hasAnyRole（"user"，"admin"）
                //.antMatchers("/", "/biz1", "/biz2").hasAnyAuthority("ROLE_common", "ROLE_admin")//admin权限
                //.antMatchers("/syslog").hasAnyAuthority("/syslog")//log权限
                //.antMatchers("/sysuser").hasAnyAuthority("/sysuser")//user权限
                //.anyRequest().authenticated();//所有请求都需要登录认证才能访问

                //方式2 使用动态配置
                //对于所有的请求都要使用access表达式中的方法进行校验
                .anyRequest().access("@UrlPermissionService.checkUrlPermission(request,authentication)");


        //3.Session管理
        http.sessionManagement()
                //always：如果当前请求没有对应的session存在，创建一个session。
                //ifRequired（默认）：在需要使用到session时才创建session
                //never：Spring Security将永远不会主动创建session，但是如果session在当前应用中已经存在，它将使用该session
                //stateless：Spring Security不会创建或使用任何session。适合于接口型的无状态应用（前后端分离），该方式节省内存资源。
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)//默认
                .invalidSessionUrl("/login.html")//回话超时，需要重新登录
                //.invalidSessionStrategy(new SessionInvalidServiceImpl())

                //migrationSession保护方式（默认）每次登录验证将创建一个新的HTTP sessionSESSION ID变了。
                //设置为“none”时，原始会话不会无效。
                //设置“newSession”后，将创建一个干净的会话，而不会复制旧会话中的任何属性。
                .sessionFixation().migrateSession()

                //一个账号多端登录被迫踢下线
                .maximumSessions(1) //限制最大登录用户数量
                .maxSessionsPreventsLogin(false) //true表示已经登录就不予许再次登录，false表示允许再次登录但是之前的登录账户会被踢下线
                .expiredSessionStrategy(sessionExpiredServiceImpl); //session超时，踢下线处理类


        http.rememberMe() //记住我
                .rememberMeCookieName("rememberMe-Cookie")//保存在浏览器cookie名称
                .tokenValiditySeconds(2 * 24 * 60 * 60)//token过期时间 2天
                .rememberMeParameter("rememberMe")//修改参数名称
                .tokenRepository(rememberMeTokenRepository());//防止项目重启导致session销毁用户需要从新登录，将token存储到MYSQL

        http.logout() //退出登录
                .logoutUrl("/logout")//默认 /logout
                .deleteCookies("JSESSIONID")//删除指定Cookies
                //方式1
                //.logoutSuccessUrl("/login.html")//默认 .loginPage("/login.html")
                //方式2
                .logoutSuccessHandler(logoutFilter);//退出自定义逻辑

        //没有权限访问
        http.exceptionHandling().accessDeniedHandler(deniedFilter);

        //关闭跨站攻击防御并且
        http.csrf().disable();
    }


    /**
     * 密码加密算法
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //方式1.创建静态的用户名密码进行权限管理
        //String password = PasswordEncoder().encode("1234");
        ////3.用户角色权限-配置某个用户拥有什么角色、拥有什么权限（动态数据库）
        //auth.inMemoryAuthentication().withUser("user").password(password).authorities("ROLE_admin", "admin");
        //auth.inMemoryAuthentication().withUser("admin").password(password).authorities("admin", "sys:log", "sys:user");
        //auth.passwordEncoder(passwordEncoder());

        //方式2.从数据库中动态加载数据进行权限管理
        auth.userDetailsService(myUserDetailsService)//用自定义的myUserDetailsService替换原有
                .passwordEncoder(passwordEncoder());//BCrypt密码加密算法

    }


    /**
     * configure(WebSecurity web)不走过滤链的放行
     * 即不通过security 完全对外的/最大级别的放行
     * 将顶目中静态资源路径开放出来
     * <p>
     * 为啥不在上面配置？
     * 这里面所有的资源不需要经过过滤器验证直接开放
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/fonts/**", "/images/**", "/js/**");
    }


    @Autowired
    private DataSource dataSource;

    /**
     * 勾选rememberMe后所有的token请求进到此方法
     * 将token存储到MYSQL中
     */
    @Bean
    public PersistentTokenRepository rememberMeTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}

