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
-- delete from emailauthuser;
-- delete from googleauthuser;

-- drop table playlist;
-- drop table userplaylistsongrelation;

select * from genre order by points desc;

update genre set points = 3 where id in(6 , 8, 20,60,46,69);


SELECT COUNT(s.id)
FROM song s
LEFT JOIN songartistRelation sar ON s.id = sar.songid
WHERE sar.songid IS NULL;





