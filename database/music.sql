SET SQL_SAFE_UPDATES = 0;



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

select * from EmailUserArtistRelation;
select * from googleuserartistrelation;
select * from passkeyuserartistrelation;


select * from EmailUserArtistRelation where userid = 1 and artistId = 10;

delete emailAuthUser from emailAuthUser where id = 6;
select * from country;


SELECT s.id , s.title , s.coverImage , s.artist
FROM song s
join songartistrelation sar on s.id = sar.songId
where sar.artistId in (
	select puar.artistid from passkeyuserartistrelation puar
    where puar.userid = 1
) order by s.points;


SELECT s.title , s.coverImage , s.album , s.artist -- getResponseAlbumPreview
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


WITH RankedSongs AS ( -- getResponseAlbumPreview
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
SELECT * FROM RankedSongs WHERE rnk = 1 limit 5;



SELECT s.id , s.title , s.coverImage , s.artist , s.points
from song s
join artist a on a.name = s.artist 
join passkeyuserartistrelation puar on puar.artistid = a.id 
where puar.userId = 1 
order by a.points desc, s.points desc;


select s.id, s.title, s.coverImage, s.artist, s.points
from song s
inner join artist a on a.name = s.artist
inner join passkeyuserartistrelation puar on puar.artistid = a.id
where puar.userId = 1
order by s.artist desc;


SELECT DISTINCT songId
FROM PasskeyUserListenHistory
WHERE date >= CURRENT_DATE - interval 3 day and userId = 1 order by rand() limit 8;

SELECT s.id , s.title , s.coverImage , s.artist , s.points
  from song s where id in (
	select songId from songartistrelation where artistId in (
		select id from artist where id in (
			select artistId from songartistrelation where songid in (14400, 17510,16624,12029,15018,14026,17793,20743)
		)
	)
) order by points desc;

SELECT song.id , song.title , song.coverImage , song.artist , song.points
  from song
  join songartistrelation sar1 on sar1.songId = song.id
  join  artist on sar1.artistId = artist.id
  join songartistrelation sar2 on artist.id = sar2.artistId
  where sar2.songId in (14400, 17510,16624,12029,15018,14026,17793,20743)
  order by song.points desc;
  
  
  SELECT count(song.id)
  from song
  join songartistrelation sar1 on song.id = sar1.songId 
  join  artist on  artist.id = sar1.artistId
  join songartistrelation sar2 on artist.id = sar2.artistId
  where sar2.songId in (14400, 17510,16624,12029,15018,14026,17793,20743)
  order by song.points desc;
  
SELECT song.id, song.title, song.coverimage, song.artist, song.album, song.points
 FROM song
 INNER JOIN songartistrelation sar1 ON  (song.id = sar1.songid)
 INNER JOIN artist ON  (artist.id = sar1.songid)
 INNER JOIN songartistrelation sar2 ON  (artist.id = sar2.artistid)
 WHERE sar2.songid IN (21435, 15518, 17793, 18836, 20743, 19373, 21745, 16624)
 ORDER BY song.points DESC;


SELECT s.id , s.title , s.coverImage , s.artist , s.points
 from song s where id in (
	select songId from SongGenreRelation where genreId in (
		select genreid from passkeyusergenrerelation where userid = 1
	)
 ) order by points desc limit 50;

SELECT s.id , s.title , s.coverImage , s.artist ,  s.genre, s.points
 from song s
 join SongGenreRelation on SongGenreRelation.songid = s.id
 join passkeyusergenrerelation on SongGenreRelation.genreId = passkeyusergenrerelation.genreid
 where passkeyusergenrerelation.userid = 1
 order by s.points desc
 limit 12;



select * from PasskeyUserAlbumRelation where userid = 1;

select * from album order by points desc;

select * from album where id in (
130 , 54	  -- select albumid from PasskeyUserAlbumRelation where userid = 1 order by points desc
) order by points;



SELECT s.id , s.title , s.coverImage , s.artist ,  s.genre, s.points , s.album
 from song s
 join songalbumartistrelation saar on saar.songid = s.id
 join album a on a.id = saar.albumId
 where a.id in (
	select albumid from PasskeyUserAlbumRelation where userid = 1 order by points desc
 ) order by a.points;


select albumid from PasskeyUserAlbumRelation where userid = 1 order by points desc limit 2;



SELECT s.title , s.coverImage , s.album , s.artist
FROM song s
JOIN SongAlbumArtistRelation sar ON s.id = sar.songId
JOIN album a ON sar.albumId = a.id
WHERE a.id IN (
    130, 54, 185, 67, 288
) order by a.points;

SELECT song.title, song.coverimage, song.artist, song.album 
FROM song 
INNER JOIN songalbumartistrelation ON  (song.id = songalbumartistrelation.songid)
 INNER JOIN album ON  (songalbumartistrelation.albumid = album.id)
 WHERE album.id IN (
 130, 54, 185, 67, 288
 )
 ORDER BY album.points ASC;



select count(*)from passkeyuserlistenhistory where userid = 1 and songid = 12029;

select  songid  , `repeat` from PasskeyUserListenHistory  where userid = 1 and date >= current_date() - interval 1 day;





SELECT song.title, song.coverimage, song.artist, song.album , artist.id , artist.profilePicUrl 
FROM song 
join artist on artist.name = song.artist
where artist.id in (
	select artistId from passkeyuserartistrelation where userid = 1
) order by artist.points;


SELECT song.title, song.coverimage, song.artist, song.album , artist.id , artist.profilePicUrl 
FROM song 
join artist on artist.name = song.artist
join passkeyuserartistrelation on passkeyuserartistrelation.artistid = artist.id
where passkeyuserartistrelation.userid = 1;



select s.id, s.title, s.coverImage, s.artist, s.points  , a.id , a.profilePicUrl
from song s
inner join artist a on a.name = s.artist
where a.id in (3 , 6 , 4);

select * from playlist;


select * from songArtistRelation;

select  distinct name from artist
join songArtistRelation on songArtistRelation.artistId = artist.id
where songArtistRelation.songId in (
 11,
426,
596,
805,
1341,
1469,
1555,
1556
);

select * from songGenreRelation;

select * from genre;

select distinct name from genre
join songGenreRelation on songGenreRelation.genreId = Genre.id
where songGenreRelation.songId in (
 11,
426,
596,
805,
1341,
1469,
1555,
1556
);



select * from artist order by points desc;


































































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