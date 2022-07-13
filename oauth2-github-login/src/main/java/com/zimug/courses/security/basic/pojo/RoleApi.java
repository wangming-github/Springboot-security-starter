package com.zimug.courses.security.basic.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色接口关系表
 * @TableName sys_role_api
 */
@TableName(value ="sys_role_api")
@Data
public class RoleApi implements Serializable {
    /**
     * 角色表自增主键
     */
    private Long roleId;

    /**
     * 接口表自增主键
     */
    private Long apiId;

    /**
     * 创建人的用户id
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}