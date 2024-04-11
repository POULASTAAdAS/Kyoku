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

select * from EmailUserAlbumRelation;
select * from PasskeyUserAlbumRelation;
select * from GoogleUserAlbumRelation;



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
   SELECT song.id , song.title , song.coverImage , song.artist , song.album,
        ROW_NUMBER() OVER (PARTITION BY a.id ORDER BY a.points DESC, song.id) AS rnk
    FROM
        song
        JOIN SongAlbumArtistRelation sar ON song.id = sar.songId
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



select * from EmailUserAlbumRelation;
select * from PasskeyUserAlbumRelation;
select * from GoogleUserAlbumRelation;

update GoogleUserAlbumRelation set points = 2 where userid = 1 and albumid = 54;


select * from album order by points desc;

select * from EmailAuthUser;

select * from googleAuthUser;
select * from googleUserPlaylist order by playlistid;




select song.id , song.title , song.artist, song.coverImage , song.album from Song 
join songalbumartistrelation on songalbumartistrelation.songId = song.id
join album on album.id = songalbumartistrelation.albumId
where album.id in (
	select albumid from GoogleUserAlbumRelation where userid = 1 order by points desc
);

select * from googleuserlistenhistory  where userid = 1 order by date desc limit 5;

select * from googleuseralbumrelation;

select * from songalbumartistrelation where albumId = 54;


-- get album
select Song.id , Song.title , Song.album , Song.artist , Song.coverImage from song
join songalbumartistrelation on songalbumartistrelation.songId = Song.id
join googleuseralbumrelation on googleuseralbumrelation.albumid = songalbumartistrelation.albumid
where googleuseralbumrelation.albumId in (
	select albumId from googleuseralbumrelation where userid = 1 order by points desc
);

-- get playlist

select * from playlist;

select * from googleuserplaylist;
select * from emailuserplaylist;
select * from passkeyuserplaylist;

select * from googleauthuser;


select Song.id , Song.title , Song.album , Song.artist , Song.coverImage , playlist.name , playlist.id from song
join passkeyUserPlaylist on passkeyUserPlaylist.songid = song.id
join playlist on playlist.id = passkeyUserPlaylist.playlistid
where passkeyUserPlaylist.songid in (
	select songid from passkeyUserPlaylist where userid = 1
) order by playlist.points desc;


select * from passkeyUserPlaylist;

select * from playlist;


select count(*) from PasskeyUserListenHistory where userid = 1;

select * from GoogleUserFavouriteTable;

select Song.id , Song.title , Song.album , Song.artist , Song.coverImage from song
join GoogleUserFavouriteTable on GoogleUserFavouriteTable.songId = Song.id
where GoogleUserFavouriteTable.userid = 1 order by GoogleUserFavouriteTable.date desc;


select * from PasskeyUserFavouriteTable;


select * from passkeyauthuser;
select * from googleauthuser;

select * from passkeyuserlistenhistory;

select * from PasskeyUserAlbumRelation;

select Album.id from Song
 join songalbumartistrelation on songalbumartistrelation.songId = Song.id
 join Album on Album.id = songalbumartistrelation.albumId
 group by Album.id
 order by Song.id asc;


select * from songalbumartistrelation;

select * from artist order by points desc;


select * from songartistrelation where artistId = 204;

select * from song 
join songartistrelation on songartistrelation.songId = Song.id
where songartistrelation.artistId = 3 order by points desc;

-- artist pagination response album 

select Song.id ,Song.album , Song.coverImage from Song
where Song.artist like '%Arijit Singh%';


select Song.id , Song.title , Song.album , Song.coverImage , Song.date from Song
where artist like '%Arijit Singh%' order by date desc
;

select songalbumartistrelation.albumId ,Song.album , Song.coverImage  , Song.date from Song
join songalbumartistrelation on songalbumartistrelation.songId = Song.id
where songalbumartistrelation.artistId = 3 order by date desc;

select * from Album where name like '%Kabir%';


select * from song where album = 'Kabir Singh';


select Song.id , Song.title , Song.album , Song.coverImage , Song.date from Song
join songalbumartistrelation on songalbumartistrelation.songId = Song.id
join album on album.id = songalbumartistrelation.albumId
where album.id = 829;



select * from passkeyauthuser;
select * from emailauthuser;

select * from passkeyuserlistenhistory;

select * from googleuserlistenhistory;


select DISTINCT googleuserlistenhistory.songid FROM googleuserlistenhistory
 WHERE (googleuserlistenhistory.userid = 1) AND (googleuserlistenhistory.`date` >= '2024-03-31T02:08:02.3295618')
 ORDER BY RAND() ASC LIMIT 8;

SELECT DISTINCT songId
FROM GoogleUserListenHistory
where googleuserlistenhistory.userid = 1 and date >= (SELECT MAX(date) - INTERVAL 3 DAY FROM GoogleUserListenHistory)
order by rand() asc limit 8;



-- fav artist mix

select * from googleuserartistrelation where userid = 1;


select name from artist 
join googleuserartistrelation on googleuserartistrelation.artistid = artist.id
where googleuserartistrelation.userid = 1;


select * from song where artist in (
	select name from artist 
	join googleuserartistrelation on googleuserartistrelation.artistid = artist.id
	where googleuserartistrelation.userid = 1
) order by points desc;



-- -------------------


select * from googleuserpinnedplaylist;
select * from googleuserpinnedalbum;
select * from googleuserpinnedartist;



select * from song;

select * from artist;


select * from googleuserplaylist order by playlistid ;

select * from passkeyuserplaylist order by playlistid;

select * from emailuserplaylist order by playlistId;

select * from playlist;

select * from googleuserpinnedplaylist;

select * from GoogleUserPinnedAlbum;

select * from passkeyuseralbumrelation;

select * from passkeyuserartistrelation;

select * from googleuserartistrelation;

select * from googleauthuser;



select artistId from googleuserartistrelation;

select * from artist 
join googleuserartistrelation on googleuserartistrelation.artistid = artist.id
where googleuserartistrelation.artistid and googleuserartistrelation.userid = 1;



select * from album 
join googleuseralbumrelation on googleuseralbumrelation.albumid = album.id
where googleuseralbumrelation.userid = 1;

select* from passkeyauthuser;

select * from passkeyuserplaylist where playlistId = 8;


select * from song where id = 100;


select * from playlist;

select * from googleuserplaylist order by playlistId desc;



select songId from songalbumartistrelation where albumid = 829;

select * from song
join songalbumartistrelation on songalbumartistrelation.songId = song.id
where songalbumartistrelation.albumId = 829;


select * from playlist;


select * from passkeyuserfavourite;

select * from googleuserfavourite;


select * from googleuserpinnedplaylist;


select * from passkeyAuthUser;




select * from googleAuthUser;

select * from artist order by points desc;

























































































































































































































































































































































































































































































































































































































































































































































































































