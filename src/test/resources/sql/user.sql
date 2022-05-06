DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
id          BIGINT(20) AUTO_INCREMENT NOT NULL COMMENT '主键ID',
username    VARCHAR(50) NOT NULL COMMENT '用户名',
email       VARCHAR(50) COMMENT '邮箱',
password    VARCHAR(50) NOT NULL COMMENT '密码',
salt        VARCHAR(50) DEFAULT 'Ahao' COMMENT '密码加盐',
sex         TINYINT UNSIGNED DEFAULT 0 COMMENT '性别, 1男, 2女',
is_locked   TINYINT UNSIGNED DEFAULT 0 COMMENT '账户是否被锁定',
is_disabled TINYINT UNSIGNED DEFAULT 0 COMMENT '账户是否被禁用',
is_deleted  TINYINT UNSIGNED DEFAULT 0 COMMENT '账户是否被删除',
expire_time  DATETIME DEFAULT NULL COMMENT '账户过期时间',
create_by   BIGINT(20) DEFAULT 0 COMMENT '创建人',
update_by   BIGINT(20) DEFAULT 0 COMMENT '更新人',
create_time  DATETIME COMMENT '创建时间',
update_time  DATETIME COMMENT '更新时间',
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='用户信息表';

INSERT INTO `user` (id, username, email, password, salt, sex, create_by, update_by, create_time, update_time) VALUES
(1, 'user1', '1@qq.com', 'pw1', 'salt1', 1, -1, -1, '2019-11-11 00:00:00', now()),
(2, 'user2', '2@qq.com', 'pw2', 'salt2', 1, -1, -1, '2019-11-12 00:00:00', now()),
(3, 'user3', '3@qq.com', 'pw3', 'salt3', 2, -1, -1, '2019-11-13 00:00:00', now()),
(4, 'user4', '4@qq.com', 'pw4', 'salt4', 2, -1, -1, '2019-11-14 00:00:00', now()),
(5, 'user5', '5@qq.com', 'pw5', 'salt5', 2, -1, -1, '2019-11-15 00:00:00', now());
