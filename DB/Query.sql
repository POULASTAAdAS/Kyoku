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

delete from emailauthuser;
delete from googleauthuser;















