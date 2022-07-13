insert into RBAC.sys_user (user_name, password,phone_number)
values ('admin', '$2a$10$r0uye0WgzvfbkwZx4vQiZOrlq6JKM8wcl.RlYIuqRSAE8FF5/dfBa','18072881204'),# 密码 1234
       ('user', '$2a$10$r0uye0WgzvfbkwZx4vQiZOrlq6JKM8wcl.RlYIuqRSAE8FF5/dfBa','18352538896');


insert into RBAC.sys_role (role_name, role_code, role_desc)
values ('管理员', 'admin', '所有权限'),
       ('用户', 'common', '业务一，业务一');

insert into RBAC.sys_user_role (user_id, role_id)
values (1, 1),
       (2, 2);

insert into RBAC.sys_menu (menu_name, url)
values ('首页', '/'),
       ('用户管理', '/sysuser'),
       ('日志管理', '/syslog'),
       ('业务一', '/biz1'),
       ('业务二', '/biz2'),
       ('首页2', '/index_alt'),
       ('api_hello', '/hello');



insert into RBAC.sys_role_menu (role_id, menu_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (2, 1),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 7);


select role_code
from RBAC.sys_role
         left join RBAC.sys_user_role sur on sys_role.id = sur.role_id
         left join RBAC.sys_user su on su.id = sur.user_id
where user_name = 'user';


SELECT distinct url
FROM RBAC.sys_menu m
         LEFT JOIN RBAC.sys_role_menu rm ON m.id = rm.menu_id
         LEFT JOIN RBAC.sys_role r ON r.id = rm.role_id
WHERE r.role_code IN ('admin', 'common');