CREATE DATABASE IF NOT EXISTS SuggestionShard;

USE SuggestionShard;

CREATE TABLE
    IF NOT EXISTS Song (
        songId BIGINT PRIMARY KEY,
        popularity BIGINT NOT NULL DEFAULT 0
    );

CREATE TABLE
    IF NOT EXISTS CountryPopularSong (
        songId BIGINT NOT NULL,
        countryId INT NOT NULL,
        Primary Key (songId, countryId)
    );

CREATE TABLE
    IF NOT EXISTS ArtistPopularSong (
        songId BIGINT NOT NULL,
        artistId BIGINT NOT NULL,
        countryId INT NOT NULL,
        Primary Key (songId, artistId, countryId)
    );

CREATE TABLE
    IF NOT EXISTS YearPopularSong (
        songId BIGINT NOT NULL,
        `year` INT NOT NULL,
        Primary Key (songId, `year`)
    );