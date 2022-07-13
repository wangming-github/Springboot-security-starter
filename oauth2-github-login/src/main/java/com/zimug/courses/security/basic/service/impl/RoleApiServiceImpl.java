package com.zimug.courses.security.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zimug.courses.security.basic.mapper.RoleApiMapper;
import com.zimug.courses.security.basic.pojo.RoleApi;
import com.zimug.courses.security.basic.service.RoleApiService;
import org.springframework.stereotype.Service;

/**
* @author maizi
* @description 针对表【sys_role_api(角色接口关系表)】的数据库操作Service实现
* @createDate 2022-07-07 03:31:46
*/
@Service
public class RoleApiServiceImpl extends ServiceImpl<RoleApiMapper, RoleApi>
    implements RoleApiService{

}




