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

create table CountryGenreRelation(
	id bigint not null primary key auto_increment,
    countryId int not null,
    genreId int not null,
    foreign key (countryId) references Country(id),
    foreign key (genreId) references Genre(id),
    points bigint not null default 0
);


create table artist(
    id int PRIMARY KEY auto_increment,
    name varchar(120) UNIQUE not null,
    profilePicUrl text not null,
    country int not null references Country(id),
    genre int not null references genre(id),
    points bigint not null default 0
);

create table SongArtistRelation(
	id bigint primary key auto_increment,
    songId bigInt,
    artistId int,
    foreign key (songId) references Song(id),
    foreign key (artistId) references artist(id)
);

create table album(
	id bigint primary key auto_increment,
	name varchar(200) unique not null,
	points bigint not null default 0
);

create table SongAlbumArtistRelation(
    songId bigInt,
    artistId int,
    albumId bigInt,
    foreign key (songId) references Song(id),
    foreign key (artistId) references artist(id),
    foreign key (albumId) references Album(id),
    primary key (songId , artistId , albumId) 
);


select * from album;




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


SELECT s.id, s.title, s.coverImage, s.artist, s.album FROM song s WHERE s.id IN (
    SELECT sar.songId FROM SongAlbumArtistRelation sar WHERE sar.albumId IN (
        SELECT a.id FROM album a WHERE a.id IN (
            SELECT saar.albumId FROM SongAlbumArtistRelation saar WHERE saar.artistId IN (
                SELECT pua.artistId FROM passkeyuserartistrelation pua WHERE pua.userId = 1
            )
        )
    )
)
ORDER BY (
    SELECT points FROM album WHERE id = (
        SELECT sar.albumId FROM SongAlbumArtistRelation sar WHERE sar.songId = s.id limit 1
    )
) DESC;


SELECT s.id , s.title , s.coverImage , s.artist
FROM song s
join songartistrelation sar on s.id = sar.songId
where sar.artistId in (
	select puar.artistid from passkeyuserartistrelation puar
    where puar.userid = 1
) order by s.artist;



SELECT s.id , s.title , s.coverImage , s.artist , s.album
FROM song s
JOIN SongAlbumArtistRelation sar ON s.id = sar.songId
JOIN album a ON sar.albumId = a.id
WHERE a.id IN (
    SELECT saar.albumId
    FROM SongAlbumArtistRelation saar
    WHERE saar.artistId IN (
        SELECT pua.artistId
        FROM passkeyuserartistrelation pua
        WHERE pua.userId = 1
    )
)
ORDER BY a.points DESC limit 30;


WITH RankedSongs AS (
   SELECT s.id , s.title , s.coverImage , s.artist , s.album,
        ROW_NUMBER() OVER (PARTITION BY a.id ORDER BY a.points DESC, s.id) AS rnk
    FROM
        song s
        JOIN SongAlbumArtistRelation sar ON s.id = sar.songId
        JOIN album a ON sar.albumId = a.id
    WHERE
        a.id IN (
            SELECT saar.albumId
            FROM SongAlbumArtistRelation saar
            WHERE saar.artistId IN (
                SELECT pua.artistId
                FROM passkeyuserartistrelation pua
                WHERE pua.userId = 1
            )
        ) ORDER BY a.points DESC
)
SELECT * FROM RankedSongs WHERE rnk = 1 limit 6;



SELECT s.id , s.title , s.coverImage , s.artist , s.points
from song s
join artist a on a.name = s.artist 
join passkeyuserartistrelation puar on puar.artistid = a.id 
where puar.userId = 1 
order by s.artist , s.points;


with ResponseAlbumPreview as (
SELECT s.id , s.title , s.coverImage , s.artist , s.points, 
ROW_NUMBER() OVER (PARTITION BY s.artist ORDER BY s.points DESC) AS rnk
from song s
join artist a on a.name = s.artist 
join passkeyuserartistrelation puar on puar.artistid = a.id 
where puar.userId = 1 order by s.artist , s.points
) select * from ResponseAlbumPreview where rnk <= 5;


with ResponseAlbumPreview as (
    select
        s.id,
        s.title,
        s.coverImage,
        s.artist,
        s.points,
        row_number() over (partition by s.artist order by s.points desc) rnk
    from
        song s
    inner join
        artist a on a.name = s.artist
    inner join
        passkeyuserartistrelation puar on puar.artistid = a.id
    where 
        puar.userId = 1
    order by
        s.artist, s.points desc
) select * from ResponseAlbumPreview where rnk <= 5;

select
        s.id,
        s.title,
        s.coverImage,
        s.artist,
        s.points
    from
        song s
    inner join
        artist a on a.name = s.artist
    inner join
        passkeyuserartistrelation puar on puar.artistid = a.id
    where 
        puar.userId = 1
    order by s.artist, s.points desc ;

































































































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