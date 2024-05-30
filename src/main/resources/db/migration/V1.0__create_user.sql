CREATE TABLE `user`
(

    `id`          int not null auto_increment primary key,
    `open_id`     varchar(128),
    `union_id`    varchar(128),
    `nickname`   varchar(128),
    `avatar`  varchar(512),
    `session_key` varchar(128),
    `create_time` datetime default current_timestamp,
    `update_time` datetime default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;