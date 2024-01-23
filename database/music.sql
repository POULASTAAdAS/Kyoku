create database music;
use music;

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

select distinct genre from song;