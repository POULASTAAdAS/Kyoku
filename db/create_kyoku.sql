create database if not exists Kyoku;
use Kyoku;

create table if not exists Country(
	id int primary key auto_increment,
    country varchar(40) unique not null
);