drop database if exists RBAC;
create database if not exists RBAC;


create table if not exists RBAC.sys_user
(
    id                 bigint auto_increment comment '主键' primary key,
    user_name          varchar(64)                 not null comment '用户名',
    nick_name          varchar(64) default 'NULL'  null comment '昵称',
    password           varchar(64)                 not null comment '密码',
    enabled            tinyint(1)  default '1'     not null comment '账号状态（1正常 0停用 固定名称）',
    account_non_locked tinyint(1)  default '1'     not null comment '账号锁定状态（1正常 0多次失败登录被锁定 固定名称）',
    email              varchar(64)                 null comment '邮箱',
    phone_number       varchar(32)                 null comment '手机号',
    sex                char                        null comment '用户性别（0男，1女，2未知）',
    avatar             varchar(128)                null comment '头像',
    create_by          bigint                      null comment '创建人的用户id',
    create_time        datetime    default (now()) null comment '创建时间',
    update_by          bigint                      null comment '更新人',
    update_time        datetime                    null comment '更新时间',
    del_flag           int         default 0       null comment '删除标志（0代表未删除，1代表已删除）'
) comment '用户表';


create table if not exists RBAC.sys_role
(
    id          bigint auto_increment comment '主键' primary key,
    role_name   varchar(32) default 'NULL'  not null comment '角色名称(汉字)',
    role_code   varchar(32) default 'NULL'  not null comment '角色英文名称(英文)',
    role_desc   varchar(64)                 null comment '角色描述',
    create_by   bigint                      null comment '创建人的用户id',
    create_time datetime    default (now()) null comment '创建时间',
    update_by   bigint                      null comment '更新人',
    update_time datetime                    null comment '更新时间',
    del_flag    int         default 0       null comment '删除标志（0代表未删除，1代表已删除）'
) comment '角色表';


create table if not exists RBAC.sys_user_role
(
    user_id     bigint                   null comment '用户表自增主键',
    role_id     bigint                   null comment '角色表自增主键',
    create_by   bigint                   null comment '创建人的用户id',
    create_time datetime default (now()) null comment '创建时间',
    update_by   bigint                   null comment '更新人',
    update_time datetime                 null comment '更新时间',
    del_flag    int      default 0       null comment '删除标志（0代表未删除，1代表已删除）'
) comment '用户角色关系表';

select role_code
from RBAC.sys_user u,
     RBAC.sys_role r,
     RBAC.sys_user_role ur
where r.id = ur.role_id
  AND u.id = ur.user_id;

create table if not exists RBAC.sys_menu
(
    id          bigint auto_increment comment '主键' primary key,
    menu_name   varchar(64)              null comment '菜单名称',
    url         varchar(64)              null comment '菜单url',
    create_by   bigint                   null comment '创建人的用户id',
    create_time datetime default (now()) null comment '创建时间',
    update_by   bigint                   null comment '更新人',
    update_time datetime                 null comment '更新时间',
    del_flag    int      default 0       null comment '删除标志（0代表未删除，1代表已删除）'
) comment '系统菜单表';

create table if not exists RBAC.sys_role_menu
(
    role_id     bigint                   null comment '角色表自增主键',
    menu_id     bigint                   null comment '菜单表自增主键',
    create_by   bigint                   null comment '创建人的用户id',
    create_time datetime default (now()) null comment '创建时间',
    update_by   bigint                   null comment '更新人',
    update_time datetime                 null comment '更新时间',
    del_flag    int      default 0       null comment '删除标志（0代表未删除，1代表已删除）'
) comment '角色菜单关系表';


select *
from RBAC.sys_role r,
     RBAC.sys_menu m,
     RBAC.sys_role_menu rm
where r.id = rm.role_id
  AND m.id = rm.menu_id;


create table if not exists RBAC.sys_api
(
    id          bigint auto_increment comment '主键' primary key,
    api_name    varchar(64)              null comment '接口名称',
    url         varchar(64)              null comment '接口url',
    create_by   bigint                   null comment '创建人的用户id',
    create_time datetime default (now()) null comment '创建时间',
    update_by   bigint                   null comment '更新人',
    update_time datetime                 null comment '更新时间',
    del_flag    int      default 0       null comment '删除标志（0代表未删除，1代表已删除）'
) comment '系统接口表';

create table if not exists RBAC.sys_role_api
(
    role_id     bigint                   null comment '角色表自增主键',
    api_id      bigint                   null comment '接口表自增主键',
    create_by   bigint                   null comment '创建人的用户id',
    create_time datetime default (now()) null comment '创建时间',
    update_by   bigint                   null comment '更新人',
    update_time datetime                 null comment '更新时间',
    del_flag    int      default 0       null comment '删除标志（0代表未删除，1代表已删除）'
) comment '角色接口关系表';


select *
from RBAC.sys_role r,
     RBAC.sys_api a,
     RBAC.sys_role_api ra
where r.id = ra.role_id
  AND a.id = ra.api_id;
