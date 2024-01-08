create database musicDB;
use musicDB;

drop table song;

create table song(
	id bigInt primary key auto_increment,
    coverImage text not null,
    masterPlaylistPath text not null,
    totalTime text not null,

    title text not null,
	artist text not null default('Kyoku'),
    album text not null default('Kyoku'),
    genre text not null default('Kyoku'),
    composer text not null default('Kyoku'),
    publisher text not null default('Kyoku'),
    album_artist text not null default('Kyoku'),
	description text not null default('Kyoku'),
	track text not null default('Kyoku'),
	date text not null
);


create table emailAuthUser(
	user_id bigint primary key auto_increment,
    username text not null,
	email varchar(320) unique not null,
    password text not null
);


drop table emailAuthUser;

select * from emailAuthUser;
select * from GoogleAuthUser;

select distinct album from song;

select * from song where (album like '%Rozaana%') or (album like '%Tiger 3%') or ( album like '%Heropanti 2%') or (album like '%Animal%');

-- Rehnuma - PagalNew ,Ruaan - PagalNew , Satranga - PagalNew , Saari Duniya Jalaa Denge - PagalNew

select * from song where title like '%Rozaana%' and album like "%Rozaana%";

select * from song where album like '%Tiger 3%' and title like '%Ruaan%';

select title from song where album like '%Whistle Baja 2.0 Heropanti 2%';

select * from song where album like '%Tiger 3%';

SELECT * FROM song WHERE (song.title LIKE '%Mauka Parast%') AND (song.album LIKE '%Heropanti 2%');
























