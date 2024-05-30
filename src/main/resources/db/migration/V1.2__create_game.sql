CREATE TABLE `game`
(

    `id`           int          not null auto_increment primary key,
    `game_id`      varchar(128) not null unique,
    `user_id`      int not null,
    `asset_change` int not null,
    `type`         varchar(128) not null,
    `operator_id`  int not null,
    `create_time`  datetime default current_timestamp,
    `update_time`  datetime default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;