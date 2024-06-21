CREATE TABLE `weixin_user`
(

    `id`          int unsigned     not null auto_increment primary key,
    `open_id`     varchar(128)     not null unique,
    `union_id`    varchar(128),
    `nickname`    varchar(128),
    `avatar`      varchar(512),
    `session_key` varchar(128),
    `is_admin`    tinyint unsigned not null default 0,
    `is_deleted`  tinyint unsigned not null default 0,
    `create_time` datetime                  default current_timestamp,
    `update_time` datetime                  default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


CREATE TABLE `weixin_user_asset`
(

    `id`          int unsigned not null auto_increment primary key,
    `user_id`     int unsigned not null unique,
    `copper_coin` int          not null default 0,
    `silver_coin` int          not null default 0,
    `gold_coin`   int          not null default 0,
    `create_time` datetime              default current_timestamp,
    `update_time` datetime              default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


CREATE TABLE `weixin_game`
(
    `id`          int unsigned not null auto_increment primary key,
    `game_type`   varchar(128) not null,
    `recorder_id` int unsigned not null,
    `score`       int unsigned not null,
    `score_ext`   varchar(128) not null,
    `winer_case`  varchar(512) not null,
    `create_time` datetime default current_timestamp,
    `update_time` datetime default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


CREATE TABLE `weixin_game_detail`
(
    `id`          int unsigned not null auto_increment primary key,
    `game_id`     int unsigned,
    `user_id`     int unsigned not null,
    `detail_type` varchar(128) not null,
    `score`       int          not null,
    `create_time` datetime default current_timestamp,
    `update_time` datetime default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;