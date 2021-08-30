use toolsetusercenter;

create table user(
    uid int not null unique auto_increment,
    name varchar(18) character set utf8mb4 collate utf8mb4_0900_ai_ci not null,
    photo int not null,
    auth int not null,
    expire datetime not null,
    email varchar(254) character set utf8 collate utf8_general_ci null,
    bcrypt char(60) character set ascii not null,
    primary key(uid)
);

