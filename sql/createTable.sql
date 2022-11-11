-- auto-generated definition
# 用户表
create table user
(
    id           bigint auto_increment
        primary key,
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       not null comment '密码',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 null comment '状态 0-正常',
    phone        varchar(128)                       null comment '电话',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    tags         varchar(1024)                      null comment '标签列表,json格式',
    userRole     tinyint  default 0                 null comment '是否是管理员',
    profile      varchar(512)                       null comment '个人简介'
)
    comment '用户表';
# 队伍表
create table team
(
    id          bigint auto_increment comment 'id' primary key,
    name        varchar(256)                       not null comment '队伍名称',
    description varchar(1024)                      null comment '描述',
    maxNum      int      default 1                 not null comment '最大人数',
    expireTime  datetime                           null comment '过期时间',
    userId      bigint comment '用户id',
    status      int      default 0                 not null comment '0 - 公开，1 - 私有，2 - 加密',
    password    varchar(512)                       null comment '密码',

    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '队伍';
# 用户队伍关系表
create table user_team
(
    id         bigint auto_increment comment 'id' primary key,
    userId     bigint comment '用户id',
    teamId     bigint comment '队伍id',
    joinTime   datetime                           null comment '加入时间',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '用户队伍关系';

create table tag
(
    id         bigint auto_increment primary key,
    tagName    varchar(256)                       null comment '用户昵称',
    userId     bigint                             null comment '用户Id',
    parentId   bigint                             null comment '父标签id',
    isParent   tinyint                            null comment '0-不是，1-父标签',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '标签';

INSERT INTO `yupi`.`user` (`id`, `username`, `userAccount`, `avatarUrl`, `gender`, `userPassword`, `email`,
                           `userStatus`, `phone`, `createTime`, `updateTime`, `isDelete`, `userRole`)
VALUES (1, 'dogYupi', '123', 'https://gw.alipayobjects.com/zos/bmw-prod/28eebf00-11cc-41bc-9c5f-3fdaabd7caeb.svg', 0,
        'xxx', '123', 0, '456', '2022-04-25 23:32:34', '2022-04-26 22:12:48', 0, 0);
INSERT INTO `yupi`.`user` (`id`, `username`, `userAccount`, `avatarUrl`, `gender`, `userPassword`, `email`,
                           `userStatus`, `phone`, `createTime`, `updateTime`, `isDelete`, `userRole`)
VALUES (5, 'meng', 'meng', 'https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png', 1,
        '0d4940d12cf6335790454c7e46fd3e6d', '1509067555@qq.com', 0, '15588352518', '2022-04-26 15:45:58',
        '2022-04-27 17:38:50', 0, 1);
INSERT INTO `yupi`.`user` (`id`, `username`, `userAccount`, `avatarUrl`, `gender`, `userPassword`, `email`,
                           `userStatus`, `phone`, `createTime`, `updateTime`, `isDelete`, `userRole`)
VALUES (12, 'mengAdmin', 'mengAdmin', 'https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png',
        NULL, '0d4940d12cf6335790454c7e46fd3e6d', NULL, 0, NULL, '2022-04-27 21:21:58', '2022-04-28 10:23:02', 0, 0);
INSERT INTO `yupi`.`user` (`id`, `username`, `userAccount`, `avatarUrl`, `gender`, `userPassword`, `email`,
                           `userStatus`, `phone`, `createTime`, `updateTime`, `isDelete`, `userRole`)
VALUES (13, 'yupi', 'yupi', 'https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png', NULL,
        '0d4940d12cf6335790454c7e46fd3e6d', NULL, 0, NULL, '2022-04-27 22:59:35', '2022-04-28 10:23:02', 0, 0);

