## Table
create table weixin.mahjong_user
(
    id          int auto_increment
        primary key,
    name        varchar(128)                       not null,
    avatar      varchar(128)                       not null,
    assets      int                                not null,
    create_time datetime default (now())           not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create table weixin.mahjong_log
(
    id          int auto_increment
        primary key,
    message     varchar(1024)                      not null,
    create_time datetime default (now())           not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

## Content
INSERT INTO mahjong_user (avatar, assets, name) VALUES ('/images/bank.png', 0, '银行');
INSERT INTO mahjong_user (avatar, assets, name) VALUES ('/images/rain.jpg', 0, '小雨');
INSERT INTO mahjong_user (avatar, assets, name) VALUES ('/images/6.jpg', 0, '小六');
INSERT INTO mahjong_user (avatar, assets, name) VALUES ('/images/xl.jpg', 0, '小刘');
INSERT INTO mahjong_user (avatar, assets, name) VALUES ('/images/ss.jpg', 0, '珊珊');
INSERT INTO mahjong_user (avatar, assets, name) VALUES ('/images/7.jpg', 0, '七总');
INSERT INTO mahjong_user (avatar, assets, name) VALUES ('/images/cc.jpg', 0, '阿翰');
INSERT INTO mahjong_user (avatar, assets, name) VALUES ('/images/sj.jpg', 0, '三金');
INSERT INTO mahjong_user (avatar, assets, name) VALUES ('/images/jj.jpg', 0, '静静');
