CREATE DATABASE IF NOT EXISTS PagingShard;

USE PagingShard;

CREATE TABLE
    IF NOT EXISTS Artist (
        id BIGINT PRIMARY KEY,
        `name` VARCHAR(60) UNIQUE NOT NULL,
        cover VARCHAR(200) DEFAULT NULL,
        popularity BIGINT NOT NULL DEFAULT 0
    );

CREATE TABLE
    IF NOT EXISTS Song (
        id BIGINT PRIMARY KEY,
        title VARCHAR(150) NOT NULL,
        poster VARCHAR(250) DEFAULT NULL,
        releaseYear INT NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS Album (
        id BIGINT PRIMARY KEY,
        title VARCHAR(150) NOT NULL,
        poster VARCHAR(250) DEFAULT NULL,
        releaseYear INT NOT NULL,
        popularity BIGINT NOT NULL DEFAULT 0
    );

CREATE TABLE
    IF NOT EXISTS ArtistSong (
        songId BIGINT REFERENCES Song (id) ON DELETE CASCADE,
        artistId BIGINT REFERENCES Artist (id) ON DELETE CASCADE,
        Primary Key (songId, artistId)
    );

CREATE TABLE
    IF NOT EXISTS ArtistAlbum (
        albumId BIGINT REFERENCES Album (id) ON DELETE CASCADE,
        artistId BIGINT REFERENCES Artist (id) ON DELETE CASCADE,
        Primary Key (albumId, artistId)
    );