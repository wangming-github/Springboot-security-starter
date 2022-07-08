package com.zimug.courses.security.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zimug.courses.security.basic.pojo.User;
import com.zimug.courses.security.basic.service.UserService;
import com.zimug.courses.security.basic.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author maizi
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2022-07-07 22:34:21
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




