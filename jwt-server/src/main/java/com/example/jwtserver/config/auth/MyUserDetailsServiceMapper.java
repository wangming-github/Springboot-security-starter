package com.example.jwtserver.config.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MyUserDetailsServiceMapper {

    ////根据userID查询用户信息
    //@Select("SELECT user_name,password,enabled\n" +//<br>
    //        "FROM sys_user u\n" +//<br>
    //        "WHERE u.user_name = #{userId} or u.phone = #{userId}")
    //MyUserDetails findByUserName(@Param("userId") String userId);
    //
    ////根据userID查询用户角色列表
    //@Select("SELECT role_code\n" +//<br>
    //        "FROM sys_role r\n" +//<br>
    //        "LEFT JOIN sys_user_role ur ON r.id = ur.role_id\n" + //<br>
    //        "LEFT JOIN sys_user u ON u.id = ur.user_id\n" + //<br>
    //        "WHERE u.user_name" + " = #{userId} or u.phone = #{userId}")
    //List<String> findRoleByUserName(@Param("userId") String userId);
    //
    //
    ////根据用户角色查询用户权限
    //@Select({"<script>", //<br>
    //        "SELECT url ",//<br>
    //        "FROM sys_menu m ", //<br>
    //        "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id ", //<br>
    //        "LEFT JOIN sys_role r ON r.id = rm.role_id ",//<br>
    //        "WHERE r.role_code IN ",//<br>
    //        "<foreach collection='roleCodes' item='roleCode' open='(' separator=',' close=')'>", //<br>
    //        "#{roleCode}",//<br>
    //        "</foreach>", //<br>
    //        "</script>"})
    //List<String> findAuthorityByRoleCodes(@Param("roleCodes") List<String> roleCodes);


    //----------------------------------------------------------------

    /**
     * 根据用户名查询用户
     *
     * @param userId 用户登录信息 并非id
     * @return 角色list
     */
    @Select({"select  user_name ,password ,enabled, account_non_locked " + //br
            "from sys_user u " + //br
            "where u.user_name = #{userId} OR u.phone_number = #{userId} ;"})
    MyUserDetails findByUserName(@Param("userId") String userId);


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
    List<String> findRoleByUserName(@Param("userId") String userId);


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
    List<String> findAuthorityByRoleCodes(@Param("roleCodes") List<String> roleCodes);


}