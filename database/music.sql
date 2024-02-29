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
	date text not null,
    points bigint not null default 0
);

SET SQL_SAFE_UPDATES = 0;



create table Country(
id int primary key auto_increment,
name varchar(200) not null unique
);


create table genre(
id int primary key auto_increment,
name varchar(120) unique not null
);


create table artist(
    id int PRIMARY KEY auto_increment,
    name varchar(120) UNIQUE not null,
    profilePicUrl text not null,
    country int not null references Country(id),
    genre int not null references genre(id),
    points bigint not null default 0
);

create table CountryGenreRelation(
	id bigint not null primary key auto_increment,
    countryId int not null,
    genreId int not null,
    foreign key (countryId) references Country(id),
    foreign key (genreId) references Genre(id)
);


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

select * from emailuserartistrelation;
select * from googleuserartistrelation;
select * from passkeyuserartistrelation;


select * from genre;
select * from country;
select * from artist;

select * from artist where country = 1 order by points desc;

select * from CountryGenreRelation;
select * from CountryGenreRelation where countryId = 1;


select * from playlist;

delete passkeyauthuser from passkeyauthuser where id = 1;

















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

 
select playlist.name from playlist;
 
select playlistrelation.songId from playlistrelation where playlistrelation.playlistid in (select playlist.id from playlist);
 
select coverimage , title , artist , playlist.name from song , playlist where id in (select playlistrelation.songId from playlistrelation where playlistrelation.playlistid in (select playlist.id from playlist));


SELECT s.coverImage , s.title , s.artist,
p.name
FROM song s
JOIN playlistrelation pr ON s.id = pr.songid
JOIN playlist p ON pr.playlistid = p.id;

SELECT song.id, song.coverimage, song.title, song.artist, playlist.name FROM song JOIN playlistrelation ON song.id = playlistrelation.songId JOIN playlist ON playlistrelation.playlistId = playlist.id;