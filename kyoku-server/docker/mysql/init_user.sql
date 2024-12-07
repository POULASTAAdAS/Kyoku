create database if not exists kyokuUser;
use kyokuUser;


create table if not exists User
(
    id            bigint primary key auto_increment,
    userType      varchar(7)   not null,
    username      varchar(320) not null,
    email         varchar(320) not null,
    passwordHash  varchar(700) not null,
    profilePicUrl varchar(700) default null,
    bDate         date         default null,
    countryId     int          not null,

    unique key (userType, email)
);

create table if not exists UserJWTRelation
(
    userId       bigint unique references `User` (id) on delete cascade,
    refreshToken varchar(2000) not null
);

create table if not exists EmailVerification
(
    userId   bigint unique references `User` (id) on delete cascade,
    `status` bool not null default false
);