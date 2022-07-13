# 固定表名 persistent_logins
drop table if exists RBAC.persistent_logins;
create table if not exists RBAC.persistent_logins
(
    username  varchar(64) NOT NULL comment '用户名',
    series    varchar(64) NOT NULL primary key comment '序列号',
    token     varchar(64) NOT NULL comment 'token',
    last_used timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '上次使用时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 comment '用户登录持久化token表';
