CREATE TABLE `weixin_user`
(

    `id`          int          not null auto_increment primary key,
    `open_id`     varchar(128) not null unique,
    `union_id`    varchar(128) not null unique,
    `nickname`    varchar(128),
    `avatar`      varchar(512),
    `admin`       tinyint      not null default 0,
    `session_key` varchar(128),
    `create_time` datetime              default current_timestamp,
    `update_time` datetime              default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

