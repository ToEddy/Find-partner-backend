-- auto-generated definition
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    username     varchar(255) null comment '用户昵称',
    userAccount  varchar(255) null comment '账号',
    avatar       varchar(1024) null comment '用户头像',
    gender       tinyint null comment '性别',
    userPassword varchar(255) null comment '密码',
    phone        varchar(128) null comment '手机号',
    email        varchar(255) null comment '邮箱',
    userStatus   tinyint  default 0 not null comment '用户状态',
    userRole     int      default 0 not null comment '用户角色 （0-普通用户）（1-管理员）（2-最高管理员）',
    isDelete     tinyint  default 0 not null comment '是否删除(逻辑删除)',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间'
) comment '用户表';
alter table user
    add column tags varchar(1024) null comment '用户标签';
alter table user
    add column userIntro varchar(1024) null comment '用户简介';

-- auto-generated definition
create table tag
(
    id         bigint auto_increment comment 'id'
        primary key,
    userId     int null comment '上传标签的用户的 id',
    tagName    varchar(255) null comment '标签名',
    tagId      int null comment '标签 id ',
    parentId   int null comment '父标签 id',
    isParent   tinyint null comment '是否为父标签 （0-不是父标签 1-父标签）',
    isDelete   tinyint  default 0 not null comment '是否删除(逻辑删除)',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null comment '更新时间',
    constraint idx_tagName
        unique (tagName) comment '标签名唯一索引'
) comment '标签表';

create index idx_userId
    on tag (userId) comment '用户id的普通索引';

create table team
(
    id          bigint auto_increment comment 'id'
        primary key,
    name        varchar(256)       not null comment '队伍名称',
    description varchar(1024) null comment '描述',
    maxNum      int      default 1 not null comment '最大人数',
    expireTime  datetime null comment '过期时间',
    userId      bigint comment '用户id',
    status      int      default 0 not null comment '0 - 公开，1 - 私有，2 - 加密',
    password    varchar(512) null comment '密码',

    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete    tinyint  default 0 not null comment '是否删除'
) comment '队伍';

-- 当数据库id 的自增值已经产生脱节的时候，我们可以设置起始值
alter table user AUTO_INCREMENT=1;

update user
set username  = ?,
    avatar    = ?,
    userIntro = ?,
    gender    = convert('0', signed),
    phone     = '22222222',
    email     = ?
where id = 3