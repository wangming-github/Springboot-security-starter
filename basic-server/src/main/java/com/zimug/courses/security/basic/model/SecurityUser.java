package com.zimug.courses.security.basic.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 思路
 * 通过set方法为UserDetails赋值
 * get方法交给Spring security调用
 *
 * @author maizi
 */
@Setter
@Slf4j
@ToString
public class SecurityUser implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    private String username;//用户名
    private String password;//密码
    private boolean accountNonExpired;//是否没过期
    private boolean accountNonLocked; //是否没被锁定
    private boolean credentialsNonExpired;//是否没过期
    private boolean enabled;//账号是否可用
    private Collection<? extends GrantedAuthority> authorities;//用户的权限集台


    public SecurityUser() {
    }

    public SecurityUser(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    // ***************************交给Spring security使用数据*************************************
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        log.info("账户是否没被锁定:{}", this.accountNonLocked);
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 通过 enabled 字段来禁止用户登录
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

}

