package com.zimug.courses.security.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zimug.courses.security.basic.pojo.Menu;
import com.zimug.courses.security.basic.service.MenuService;
import com.zimug.courses.security.basic.mapper.MenuMapper;
import org.springframework.stereotype.Service;

/**
* @author maizi
* @description 针对表【sys_menu(系统菜单表)】的数据库操作Service实现
* @createDate 2022-07-07 03:31:46
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

}




