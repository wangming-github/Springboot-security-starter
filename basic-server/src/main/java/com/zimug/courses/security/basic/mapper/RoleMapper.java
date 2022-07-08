package com.zimug.courses.security.basic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.zimug.courses.security.basic.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author maizi
 * @description 针对表【sys_role(角色表)】的数据库操作Mapper
 * @createDate 2022-07-07 03:31:46
 * @Entity com.zimug.courses.security.basic.pojo.Role
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {


}




