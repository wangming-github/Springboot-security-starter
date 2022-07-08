package com.zimug.courses.security.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zimug.courses.security.basic.pojo.Role;
import com.zimug.courses.security.basic.service.RoleService;
import com.zimug.courses.security.basic.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author maizi
 * @description 针对表【sys_role(角色表)】的数据库操作Service实现
 * @createDate 2022-07-07 03:31:46
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}




