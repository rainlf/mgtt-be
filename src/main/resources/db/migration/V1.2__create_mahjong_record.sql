CREATE TABLE `weixin_mahjong_record`
(
    `id`          int          not null auto_increment primary key,
    `round_id`    varchar(128) not null,
    `recorder_id` int          not null,
    `type`        varchar(128) not null,
    `user_id`     int          not null,
    `score`       int          not null,
    `is_deleted`  tinyint      not null default 0,
    `create_time` datetime              default current_timestamp,
    `update_time` datetime              default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;