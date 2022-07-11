package com.zimug.config;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 自定义UserDetailsService对应mapper
 *
 * @author maizi
 */
public interface MyUserDetailsServiceMapper {


    /**
     * 根据用户名查询用户
     *
     * @param userId 用户登录信息 并非id
     * @return 角色list
     */
    @Select({"select  user_name ,password ,enabled, account_non_locked " + //br
            "from sys_user u " + //br
            "where u.user_name = #{userId} OR u.phone_number = #{userId} ;"})
    MyUserDetails findUserByUsername(@Param("userId") String userId);


    /**
     * 根据用户查询角色
     *
     * @param userId 用户
     * @return 角色list
     */
    @Select({"select role_code " + //br
            "from sys_role " + //br
            "left join sys_user_role sur on sys_role.id=sur.role_id " + //br
            "left join sys_user su on su.id = sur.user_id " + //br
            "where user_name = #{userId} OR phone_number = #{userId} ;"})
    List<String> findRoleCodeByUsername(@Param("userId") String userId);


    /**
     * 根据用户角色查询用户权限（菜单访问权限）
     *
     * @param roleCodes 角色code
     * @return url List<String>
     */
    @Select({"<script>", //br
            "SELECT distinct url " + //br
                    "FROM sys_menu m " + //br
                    "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id " + //br
                    "LEFT JOIN sys_role r ON r.id = rm.role_id " + //br
                    "WHERE r.role_code IN ", //br
            "<foreach collection='roleCodes' index='index' item='roleCode' open='(' separator=',' close=')'>", //br
            "#{roleCode}", //br
            "</foreach> ", //br
            "</script>"})
    List<String> findAuthorityByUsername(@Param("roleCodes") List<String> roleCodes);


    @Update({" UPDATE sys_user u" + //br
            "  SET u.enabled = #{enabled} " + //br
            " WHERE u.user_name = #{username}"})
    int updateEnabledByUsername(MyUserDetails myUserDetails);


    @Update({" UPDATE sys_user u" + //br
            "  SET u.account_non_locked = #{accountNonLocked} " + //br
            " WHERE u.user_name = #{username}"})
    int updateAccountNonLockedByUsername(MyUserDetails myUserDetails);


}
