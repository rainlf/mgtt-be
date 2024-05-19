CREATE TABLE `user`
(

    `id`          int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`        varchar(128),
    `avatar`      varchar(512),
    `session_key` varchar(128)

) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;