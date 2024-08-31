use kyoku;

SET SQL_SAFE_UPDATES = 0;

select * from country;
select * from genre;
select * from artist;
select * from album;
select * from song;

select * from ArtistCountryRelation;
select * from ArtistGenreRelation;
select * from ArtistAlbumRelation;

select * from SongArtistRelation;
select * from SongAlbumRelation;


select count(*) from ArtistCountryRelation;
select count(*) from ArtistGenreRelation;
select count(*) from ArtistAlbumRelation;


select * from emailauthuser;
select * from googleauthuser;
delete from googleauthuser;

select * from loginverificationmail;

select * from playlist;
select * from userplaylistsongrelation;
select * from usergenrerelation;
select * from userartistrelation;

delete from playlist;
delete from usergenrerelation;
delete from userartistrelation;





-- getPopularSongMix
select * from song
join songcountryrelation on songcountryrelation.songId = song.id
where songcountryrelation.countryId = 1
order by song.points desc;

-- getPopularSongFromUserTime
select song.id , song.title , song.coverImage , song.year from song
join songcountryrelation on songcountryrelation.songId = song.id
where songcountryrelation.countryId = 1 and song.year in (2016 , 2017 , 2018 , 2019)
order by song.points desc limit 1;

select song.id , song.title , song.coverImage , song.year from song
join songcountryrelation on songcountryrelation.songId = song.id
where songcountryrelation.countryId = 1 and song.year = 2018
order by song.points desc limit 1;

WITH RankedSongs AS (
    SELECT 
        song.id, 
        song.title, 
        song.coverImage, 
        song.year,
        ROW_NUMBER() OVER (PARTITION BY song.year ORDER BY song.points DESC) as rn
    FROM song
    JOIN songcountryrelation ON songcountryrelation.songId = song.id
    WHERE songcountryrelation.countryId = 1 AND song.year IN (2016, 2017, 2018, 2019)
)
SELECT 
    id, 
    title, 
    coverImage, 
    year 
FROM RankedSongs 
WHERE rn = 1;





-- getFavouriteArtistMix
select song.id , song.title , song.coverImage , song.points from song
join songartistrelation on songartistrelation.songId = song.id
join artist on artist.id = songartistrelation.artistId
join userartistrelation on userartistrelation.artistid = artist.id
where userartistrelation.userid = 9 and userartistrelation.usertype = "GOOGLE_USER"
order by rand() asc ,song.points desc limit 50;


-- getDayTypeSong
SELECT DISTINCT song.id, 
                song.title, 
                song.coverImage, 
                song.points 
FROM song
JOIN songcountryrelation 
  ON songcountryrelation.songId = song.id
WHERE songcountryrelation.countryId = 1 
  AND (song.title LIKE '%lofi%' OR song.title LIKE '%remix%')
ORDER BY CASE 
           WHEN song.title LIKE '%lofi%' THEN 1 
           ELSE 2 
         END, 
         song.points DESC limit 40;


 
-- getPopularAlbum
select distinct album.id , album.name , song.coverImage , album.points from song
join songalbumrelation on songalbumrelation.songId = song.id
join album on album.id = songalbumrelation.albumId
join albumcountryrelation on albumcountryrelation.albumId = album.id
where albumcountryrelation.countryId = 1 order by album.points desc limit 10;


select * from emailauthuser;
select * from googleauthuser;
select * from userplaylistsongrelation;


select * from userplaylistsongrelation;

select * from playlist;
select * from userplaylistsongrelation;


select distinct(userid) from userplaylistsongrelation where playlistid = 3;
select *  from userplaylistsongrelation;

select * from useralbumrelation;
select * from useralbumrelation;


select * from userartistrelation;

delete from userartistrelation where userid = 0;


select * from artist;
select * from userartistrelation;
select count(*) from userartistrelation where artistid = 10613;

select song.id, song.title, song.coverImage , song.points , artist.name from songartistrelation
join song on song.id = songartistrelation.songId
join artist on artist.id = songartistrelation.artistId
where songartistrelation.artistId = 10613
order by song.points desc;



select album.id , album.name , song.coverImage , song.year from artistalbumrelation
join album on album.id  = artistalbumrelation.albumId
join songalbumrelation on songalbumrelation.albumId = album.id
join song on song.id = songalbumrelation.songId
where artistalbumrelation.artistId = 2967
GROUP BY album.id, album.name, song.coverImage, song.year
order by song.year desc;

SELECT album.id, album.name, MAX(song.coverImage) AS coverImage, MAX(song.year) AS year
FROM artistalbumrelation
JOIN album ON album.id = artistalbumrelation.albumId
JOIN songalbumrelation ON songalbumrelation.albumId = album.id
JOIN song ON song.id = songalbumrelation.songId
WHERE artistalbumrelation.artistId = 2967
GROUP BY album.id
ORDER BY year DESC;


select * from album order by points desc limit 30; 

select album.id , album.name , song.coverImage , song.year from song 
join songalbumrelation on songalbumrelation.songId = song.id
join album on songalbumrelation.albumId = album.id
order by song.year desc;
















































































































































































































































































































