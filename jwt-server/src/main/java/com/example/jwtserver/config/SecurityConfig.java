package com.example.jwtserver.config;

import com.example.jwtserver.config.auth.MyLogoutSuccessHandler;
import com.example.jwtserver.config.auth.MyUserDetailsService;
import com.example.jwtserver.config.auth.jwt.JwtAuthenticationTokenFilter;
import com.example.jwtserver.content.LogContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Resource
    MyUserDetailsService myUserDetailsService;

    @Resource
    private DataSource datasource;

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.cors()//???????????? @see #corsConfigurationSource()
                .and().addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)//
                .logout().logoutUrl("/signout")//
                .deleteCookies("JSESSIONID")//
                .logoutSuccessHandler(myLogoutSuccessHandler)//
                .and().rememberMe()//
                .rememberMeParameter("remember-me-new")//
                .rememberMeCookieName("remember-me-cookie")//
                .tokenValiditySeconds(2 * 24 * 60 * 60)//
                .tokenRepository(persistentTokenRepository())//
                .and().authorizeRequests()//
                .antMatchers("/authentication", "/refreshtoken").permitAll()//
                .antMatchers("/index").authenticated()//
                .anyRequest().access("@rabcService.hasPermission(request,authentication)")//
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
                //.and().csrf().disable();//CSRF->??????CSRF??????????????????
                .and().csrf()//CSRF->??????CSRF??????????????????
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())//??????CSRFToken?????????????????????cookie???
                .ignoringAntMatchers("/authentication");//???????????????CSRF?????????????????????

    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        //??????????????????????????????????????????
        web.ignoring().antMatchers("/css/**", "/fonts/**", "/img/**", "/js/**");
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {

        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(datasource);

        return tokenRepository;
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * Spring Security??????????????????????????????CORS????????????????????????
     * CorsConfigurationSource ?????????????????????????????????????????????????????????CorsFilter????????????
     *
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        log.info(LogContent.LOG_RED + "??????CorsFilter?????????");
        CorsConfiguration configuration = new CorsConfiguration();
        //???POST Headers?????????Origin == http://localhost:8888 ??????????????????
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8888"));//?????????
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));//??????????????????
        configuration.applyPermitDefaultValues();

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
