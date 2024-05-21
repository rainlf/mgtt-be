CREATE TABLE `user`
(

    `id`          int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `open_id`     varchar(128),
    `union_id`    varchar(128),
    `nick_name`   varchar(128),
    `avatar_url`  varchar(512),
    `session_key` varchar(128)

) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;