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




create table EmailUserListenHistory(
	userId bigInt references EmailAuthUser(id) on delete cascade,
	songId bigInt references Song(id),
	date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key (userId , songId , date)
);

create table GoogleUserListenHistory(
	userId bigInt references GoogleAuthUser(id) on delete cascade,
	songId bigInt references Song(id),
	date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key (userId , songId , date)
);

create table PasskeyUserListenHistory(
	userId bigInt references PasskeyAuthUser(id) on delete cascade,
	songId bigInt references Song(id),
	date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key (userId , songId , date)
);





create table SongGenreRelation(
	songId bigint references Song(id),
    genreId int references Genre(id),
    primary key (songId , genreId)
);




create table PasskeyUserAlbumRelation(
	userId bigint references PasskeyAuthUser(id) on delete cascade,
    albumId bigInt references Album(id),
    points int not null default 0,
    primary key (userId , albumId)
);


create table GoogleUserAlbumRelation(
	userId bigint references GoogleAuthUser(id) on delete cascade,
    albumId bigInt references Album(id),
    points int not null default 0,
    primary key (userId , albumId)
);


create table EmailUserAlbumRelation(
	userId bigint references EmailAuthUser(id) on delete cascade,
    albumId bigInt references Album(id),
    points int not null default 0,
    primary key (userId , albumId)
);
















































































































