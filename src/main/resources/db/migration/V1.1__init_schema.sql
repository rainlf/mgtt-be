CREATE TABLE `weixin_user`
(

    `id`          int          not null auto_increment primary key,
    `open_id`     varchar(128) not null unique,
    `union_id`    varchar(128),
    `nickname`    varchar(128),
    `avatar`      varchar(512),
    `admin`       tinyint      not null default 0,
    `is_deleted`  tinyint      not null default 0,
    `session_key` varchar(128),
    `create_time` datetime              default current_timestamp,
    `update_time` datetime              default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


CREATE TABLE `weixin_user_asset`
(

    `id`          int not null auto_increment primary key,
    `user_id`     int not null unique,
    `copper_coin` int not null default 0,
    `silver_coin` int not null default 0,
    `gold_coin`   int not null default 0,
    `create_time` datetime     default current_timestamp,
    `update_time` datetime     default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


CREATE TABLE `weixin_mahjong_round`
(
    `id`          int          not null auto_increment primary key,
    `recorder_id` int          not null,
    `baseFan`     int          not null,
    `winType`     varchar(128) not null,
    `fanTypes`    varchar(512) not null,
    `create_time` datetime default current_timestamp,
    `update_time` datetime default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


CREATE TABLE `weixin_mahjong_round_detail`
(
    `id`          int          not null auto_increment primary key,
    `recorder_id` int          not null,
    `type`        varchar(128) not null,
    `userId`      int          not null,
    `score`       int          not null,
    `site`        varchar(128) not null,
    `create_time` datetime default current_timestamp,
    `update_time` datetime default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;