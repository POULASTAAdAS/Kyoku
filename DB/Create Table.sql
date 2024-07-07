create table Country(
id int primary key auto_increment,
name varchar(100) not null unique
);

create table Genre(
id int primary key auto_increment,
name varchar(100) not null unique,
points bigint not null default(0)
);


Create table artist(
id bigint primary key auto_increment,
name varchar(400) not null unique,
coverImage varchar(800) default(null),
points bigint not null default(0)
);

Create table ArtistCountryRelation(
artistId bigint references artist(id) on delete cascade,
countryId bigint references Country(id) on delete cascade,

primary key(artistId, countryId)
);

Create table ArtistGenreRelation(
artistId bigint references artist(id) on delete cascade,
genreId bigint references Genre(id) on delete cascade,

primary key(artistId, genreId)
);

Create table Album(
id bigint primary key auto_increment,
name varchar(100) not null unique,
points bigint not null default(0)
);

Create table ArtistAlbumRelation(
artistId bigint references artist(id) on delete cascade,
albumId bigint references Album(id) on delete cascade,

primary key(artistId, albumId)
);

Create table AlbumCountryRelation(
albumId bigint references Album(id) on delete cascade,
countryId bigint references Country(id) on delete cascade,

primary key(albumId, countryId)
);


Create Table Song(
id Bigint primary key auto_increment,
title text not null,
coverImage text not null,
masterPlaylistPath text not null,
totalTime varchar(50) not null,
composer text not null default (''),
publisher text not null default (''),
album_artist text not null default (''),
track text not null default (''),
`year` int,
points bigint not null default(0)
);

Create table songArtistRelation(
songId bigint references Song(id) on delete cascade,
artistId bigint references artist(id) on delete cascade,

primary key(songId, artistId)
);

Create table songCountryRelation(
songId bigint references Song(id) on delete cascade,
countryId int references Country(id) on delete cascade,

primary key(songId, countryId)
);


Create table songAlbumRelation(
songId bigint references Song(id) on delete cascade,
albumId bigint references Album(id) on delete cascade,

primary key(songId , albumId)
);


Create table Playlist(
id Bigint primary key auto_increment,
name varchar(400) not null,
points bigint not null default(0)
);

Create Table SongPlaylistRelation(
playlistId bigint references Playlist(id) on delete cascade,
songId bigint references Song(id) on delete cascade,

primary key(playlistId , songId)
);




 