CREATE TABLE `weixin_user_asset`
(

    `id`          int not null auto_increment primary key,
    `user_id`     int not null unique,
    `copper_coin` int      default 0,
    `silver_coin` int      default 0,
    `gold_coin`   int      default 0,
    `create_time` datetime default current_timestamp,
    `update_time` datetime default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;