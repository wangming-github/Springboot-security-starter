package com.zimug.courses.security.basic.auth.security.config;

import com.zimug.courses.security.basic.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
@Slf4j
public class MethodELTestService {

    /**
     * Pre 之前
     * 在执行方法之前进行判断当前登录用户是不是 admin角色 否则就抛出 AccessDeniedException
     */
    @PreAuthorize("hasRole('admin')")
    public List<User> findAll() {
        log.info("@PreAuthorize 在执行方法之前进行判断当前登录用户是不是 admin角色 否则就抛出 AccessDeniedException");
        return null;
    }

    /**
     * Post 之后
     * 在执行方法之后 感觉方法返回值 returnObject.name是否等于当前登录认证的名称
     */
    @PostAuthorize("returnObject.userName == authentication.name")
    public User findOne() {
        String authName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user1 = new User();
        user1.setUserName("admin");
        log.info("@PostAuthorize 在执行方法之后 感觉方法返回值 returnObject.name是否等于当前登录认证的名称");
        return user1;
    }

    /**
     * Filter 过滤
     * 进入方法之前判断方法参数 ids 是否满足 filterObject%2==0
     * 是将参数传入
     * 否过滤
     */
    @PreFilter(filterTarget = "ids", value = "filterObject%2 == 0")
    public void delete(List<Integer> ids, List<String> usernames) {
        log.info("@PreFilter 进入方法之前判断方法参数 ids 是否满足 filterObject%2==0");
    }


    /**
     * Post 之后
     * 在执行方法之后 判断方法返回的List<User> 中的User的name属性是否等于当前认证的主体名
     * 相等保留
     * 不想到移除
     */
    @PostFilter("filterObject.userName ==authentication.name")
    public List<User> findAllPD() {
        List<User> list = new ArrayList<>();
        User user1 = new User();
        user1.setUserName("maizi");
        User user2 = new User();
        user2.setUserName("admin");
        list.add(user1);
        list.add(user2);
        log.info("@PostFilter 在执行方法之后 判断方法返回的List<User> 中的User的name属性是否等于当前认证的主体名");
        return list;
    }
}
