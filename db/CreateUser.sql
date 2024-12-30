CREATE DATABASE IF NOT EXISTS kyokuUser;
USE kyokuUser;

CREATE TABLE IF NOT EXISTS `User` (
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    userType      VARCHAR(7)    NOT NULL,
    username      VARCHAR(320)  NOT NULL,
    email         VARCHAR(320)  NOT NULL,
    passwordHash  VARCHAR(700)  NOT NULL,
    profilePicUrl VARCHAR(700)  DEFAULT NULL,
    bDate         DATE          DEFAULT NULL,
    countryId     INT           NOT NULL,
    UNIQUE KEY (userType, email)
);

CREATE TABLE IF NOT EXISTS UserJWTRelation (
    userId       BIGINT UNIQUE REFERENCES `User` (id) ON DELETE CASCADE,
    refreshToken VARCHAR(2000) NOT NULL
);

CREATE TABLE IF NOT EXISTS UserPinned(
	userId      BIGINT UNIQUE REFERENCES `User` (id) ON DELETE CASCADE,
    otherId		BIGINT NOT NULL,
    pinnedType	VARCHAR(9) NOT NULL,
    PRIMARY KEY (userId, otherId, pinnedType)
);

CREATE TABLE IF NOT EXISTS UserPlaylist(
	userId      BIGINT UNIQUE REFERENCES `User` (id) ON DELETE CASCADE,
    playlistId	BIGINT NOT NULL,
    PRIMARY KEY (userId, playlistId)
);


-- USER'S LIBRARY
CREATE TABLE IF NOT EXISTS UserSavedPlaylist(
	userId      BIGINT UNIQUE REFERENCES `User` (id) ON DELETE CASCADE,
    playlistId	BIGINT NOT NULL,
    PRIMARY KEY (userId, playlistId)
);

CREATE TABLE IF NOT EXISTS UserFavouriteSong(
	userId	BIGINT UNIQUE REFERENCES `User` (id) ON DELETE CASCADE,
    songId	BIGINT NOT NULL,
    PRIMARY KEY (userId, songId)
);

CREATE TABLE IF NOT EXISTS UserSavedArtist(
	userId	 	BIGINT UNIQUE REFERENCES `User` (id) ON DELETE CASCADE,
    artistId	BIGINT NOT NULL,
    PRIMARY KEY (userId, artistId)
);

CREATE TABLE IF NOT EXISTS UserSavedGenre(
	userId	 	BIGINT UNIQUE REFERENCES `User` (id) ON DELETE CASCADE,
    genreId	BIGINT NOT NULL,
    PRIMARY KEY (userId, genreId)
);

CREATE TABLE IF NOT EXISTS UserSavedAlbum(
	userId	BIGINT UNIQUE REFERENCES `User` (id) ON DELETE CASCADE,
    albumId	BIGINT NOT NULL,
    PRIMARY KEY (userId, albumId)
);


















