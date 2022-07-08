package com.zimug.courses.security.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zimug.courses.security.basic.pojo.UserRole;
import com.zimug.courses.security.basic.service.UserRoleService;
import com.zimug.courses.security.basic.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author maizi
* @description 针对表【sys_user_role(用户角色关系表)】的数据库操作Service实现
* @createDate 2022-07-07 03:31:46
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




