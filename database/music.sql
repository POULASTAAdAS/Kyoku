create database music;
use music;

create table song(
	id bigInt primary key auto_increment,
    coverImage text not null,
    masterPlaylistPath text not null,
    totalTime text not null,
    title text not null,
	artist text not null default('Kyoku'), -- references 
    album text not null default('Kyoku'),
    genre text not null default('Kyoku'),
    composer text not null default('Kyoku'),
    publisher text not null default('Kyoku'),
    album_artist text not null default('Kyoku'),
	description text not null default('Kyoku'),
	track text not null default('Kyoku'),
	date text not null
);

alter table song add column points bigint not null default 0;

select * from song;
select * from song where id = 21005;
select * from song where album = 'Animal';
select count(*) from song;

select distinct album from song;

select count(distinct genre) from song;

select distinct artist from song;

SET SQL_SAFE_UPDATES = 0;


select * from song where genre in (select distinct genre from song);

delete from emailAuthUser where id = 2;

create table genre(
id int primary key auto_increment,
genre varchar(120) unique not null
);

alter table genre add column points bigint not null default 0;

select * from genre;
select distinct(genre) from genre;
select * from genre where genre = ' Hindi Pop';

-- create table usersGenre(
-- id Long primary key,
-- genreId int,
-- userId varchar(320)
-- );

create table artist(
    id int PRIMARY KEY auto_increment,
    name varchar(120) UNIQUE not null,
    profilePicUrl text not null,
    country text not null,
    genre int not null references genre(id)
);


alter table artist add column points bigint not null default 0;

select * from artist;
select * from artist where id = 22;

select count(*) from genre where id in (select distinct(genre) from artist);



select * from emailAuthUser;
select * from googleAuthUser;
select * from passkeyAuthUser;
select * from invalidrefreshToken;
select * from sessionStorage;

select * from PasskeyUserPlaylist;
select * from GoogleUserPlaylist;
select * from EmailUserPlaylist;

select * from EmailUserGenreRelation;
select * from GoogleUserGenreRelation;
select * from PasskeyUserGenreRelation;


select * from Country;
select * from CountryGenreRelation;



select * from song where id in (1 , 2 ,3) order by title;


SELECT genre.id, genre.genre, genre.points FROM genre WHERE genre.id IN (1, 2, 3, 5, 7, 18, 40, 43, 47, 53, 58, 62, 66, 68, 76, 78, 80, 92, 94, 105, 108, 111);

SELECT distinct(genre) FROM artist WHERE artist.genre IN (1, 2, 3, 7 , 18);

select * from genre where id in (select distinct(genre) from artist);



















-- user


create table playlist(
id int primary key auto_increment,
name varchar(20) unique
);

create table playlistRelation(
id int primary key auto_increment,
playlistid int references playlist.id,
songid bigint references song.id
);



insert into playlist values
(1 , "playliat 1"),
(2 , "playliat 2");

insert into playlistrelation values
(1 , 1 , 1),
(2 , 1 , 100),
(3 , 1 , 101),
(4 , 1, 1423),
(5 , 2 , 1424),
(6 , 2 , 1423),
(7 , 2 , 423),
(8 ,2 , 123),
(9 , 2, 23);
 
drop table playlistrelation;
 
select playlist.name from playlist;
 
select playlistrelation.songId from playlistrelation where playlistrelation.playlistid in (select playlist.id from playlist);
 
select coverimage , title , artist , playlist.name from song , playlist where id in (select playlistrelation.songId from playlistrelation where playlistrelation.playlistid in (select playlist.id from playlist));


SELECT s.coverImage , s.title , s.artist,
p.name
FROM song s
JOIN playlistrelation pr ON s.id = pr.songid
JOIN playlist p ON pr.playlistid = p.id;

SELECT song.id, song.coverimage, song.title, song.artist, playlist.name FROM song JOIN playlistrelation ON song.id = playlistrelation.songId JOIN playlist ON playlistrelation.playlistId = playlist.id;