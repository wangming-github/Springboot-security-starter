package com.zimug.courses.security.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zimug.courses.security.basic.pojo.RoleMenu;
import com.zimug.courses.security.basic.service.RoleMenuService;
import com.zimug.courses.security.basic.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

/**
* @author maizi
* @description 针对表【sys_role_menu(角色菜单关系表)】的数据库操作Service实现
* @createDate 2022-07-07 03:31:46
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

}




