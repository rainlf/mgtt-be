CREATE TABLE `game`
(

    `id`           int          not null auto_increment primary key,
    `game_id`      varchar(128) not null unique,
    `user_id`      int,
    `asset_change` int,
    `type`         varchar(128),
    `operator_id`  int,
    `create_time`  datetime default current_timestamp,
    `update_time`  datetime default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;