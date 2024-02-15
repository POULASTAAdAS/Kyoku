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

select count(*) from song where genre = "Punjabi";
select count(*) from song where genre = "Tamil";

select count(*) from song;

select count(distinct genre) from song;

select distinct artist from song;

select * from song where genre = "PaglaSongs.com";

SET SQL_SAFE_UPDATES = 0;


select * from song where genre in (select distinct genre from song);

select * from emailAuthUser;
select * from googleAuthUser;
select * from passkeyAuthUser;
select * from invalidrefreshToken;
select * from sessionStorage;

select * from PasskeyUserPlaylist;
select * from GoogleUserPlaylist;
select * from EmailUserPlaylist;

delete from emailAuthUser where id = 2;

create table genre(
id int primary key auto_increment,
genre varchar(120) unique not null
);

select * from genre;
drop table genre;

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
    preferedSpeakingLang text not null
);


select distinct preferedSpeakingLang from artist;





create table playlist(
id int primary key auto_increment,
name varchar(20) unique
);

create table playlistrelation(
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





