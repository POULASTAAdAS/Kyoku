create database  if not exists kyokuUser;
use kyokuUser;

create table if not exists User(
	id bigint primary key auto_increment,
    email varchar(320) not null,
    passwordHash varchar(700) not null,
    profilePicUrl varchar(700) default null,
    bDate date default null,
    countryId int
);

create table if not exists UserJWTRelation(
	userId bigint references User(id) on delete cascade,
    refreshToken varchar(2000) not null
);