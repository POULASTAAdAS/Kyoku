CREATE DATABASE IF NOT EXISTS Kyoku;

USE Kyoku;

CREATE TABLE
    IF NOT EXISTS Country (
        id INT PRIMARY KEY AUTO_INCREMENT,
        country VARCHAR(40) UNIQUE NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS Artist (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        `name` VARCHAR(60) UNIQUE NOT NULL,
        coverImage VARCHAR(200) DEFAULT NULL,
        popularity BIGINT NOT NULL DEFAULT 0
    );

CREATE TABLE
    IF NOT EXISTS Genre (
        id INT PRIMARY KEY AUTO_INCREMENT,
        `name` VARCHAR(30) UNIQUE NOT NULL,
        cover VARCHAR(200) DEFAULT NULL,
        popularity BIGINT NOT NULL DEFAULT 0
    );

CREATE TABLE
    IF NOT EXISTS Album (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        `name` VARCHAR(100) UNIQUE NOT NULL,
        popularity BIGINT NOT NULL DEFAULT 0
    );

CREATE TABLE
    IF NOT EXISTS Song (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        title VARCHAR(150) UNIQUE NOT NULL,
        poster VARCHAR(250) DEFAULT NULL,
        masterPlaylist VARCHAR(250) NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS SongInfo (
        songId BIGINT UNIQUE REFERENCES Song (id) ON DELETE CASCADE,
        releaseYear INT NOT NULL,
        composer VARCHAR(200) DEFAULT NULL,
        popularity BIGINT NOT NULL DEFAULT 0
    );

CREATE TABLE
    IF NOT EXISTS Playlist (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        `name` varchar(100) NOT NULL,
        visibilityState BOOL DEFAULT FALSE,
        popularity BIGINT NOT NULL DEFAULT 0
    );

CREATE TABLE
    SongPlaylist (
        songId BIGINT REFERENCES Song (id) ON DELETE CASCADE,
        playlistId BIGINT REFERENCES Playlist (id) ON DELETE CASCADE,
        PRIMARY KEY (songId, playlistId)
    );

CREATE TABLE
    IF NOT EXISTS SongCountry (
        songId BIGINT REFERENCES Song (id) ON DELETE CASCADE,
        countryId INT REFERENCES Country (id) ON DELETE CASCADE,
        PRIMARY KEY (songId, countryId)
    );

CREATE TABLE
    IF NOT EXISTS SongArtist (
        songId BIGINT REFERENCES Song (id) ON DELETE CASCADE,
        artistId BIGINT REFERENCES Artist (id) ON DELETE CASCADE,
        PRIMARY KEY (songId, artistId)
    );

CREATE TABLE
    IF NOT EXISTS SongAlbum (
        songId BIGINT REFERENCES Song (id) ON DELETE CASCADE,
        albumId BIGINT REFERENCES Album (id) ON DELETE CASCADE,
        PRIMARY KEY (songId, albumId)
    );

CREATE TABLE
    IF NOT EXISTS SongGenre (
        songId BIGINT REFERENCES Song (id) ON DELETE CASCADE,
        genreId INT REFERENCES GENRE (id) ON DELETE CASCADE,
        PRIMARY KEY (songId, genreId)
    );

CREATE TABLE
    IF NOT EXISTS ArtistCountry (
        artistId BIGINT REFERENCES Artist (id) ON DELETE CASCADE,
        countryId INT REFERENCES Country (id) ON DELETE CASCADE,
        PRIMARY KEY (artistId, countryId)
    );

CREATE TABLE
    IF NOT EXISTS ArtistAlbum (
        artistId BIGINT REFERENCES Artist (id) ON DELETE CASCADE,
        albumId BIGINT REFERENCES Album (id) ON DELETE CASCADE,
        PRIMARY KEY (artistId, albumId)
    );

CREATE TABLE
    IF NOT EXISTS ArtistGenre (
        artistId BIGINT REFERENCES Artist (id) ON DELETE CASCADE,
        genreId INT REFERENCES GENRE (id) ON DELETE CASCADE,
        PRIMARY KEY (artistId, genreId)
    );