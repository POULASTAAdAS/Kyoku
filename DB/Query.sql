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

select * from loginverificationmail;

select * from playlist;
select * from userplaylistsongrelation;
select * from usergenrerelation;
select * from userartistrelation;






-- getPopularSongMix
select * from song
join songcountryrelation on songcountryrelation.songId = song.id
where songcountryrelation.countryId = 1
order by song.points desc;

-- getPopularSongFromUserTime
select * from song
join songcountryrelation on songcountryrelation.songId = song.id
where songcountryrelation.countryId = 1 and song.year = 2016
order by song.points desc;


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































































































































































































































































































































































































































