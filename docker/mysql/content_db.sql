CREATE DATABASE IF NOT EXISTS CONTENT;

USE CONTENT;

CREATE TABLE
    IF NOT EXISTS Country (
        id INT PRIMARY KEY AUTO_INCREMENT,
        country VARCHAR(40) NOT NULL,
        code VARCHAR(4) NOT NULL,
        UNIQUE KEY uq_country (country),
        UNIQUE KEY uq_code (code)
    );

CREATE TABLE
    IF NOT EXISTS SONG (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        title VARCHAR(150) NOT NULL,
        poster VARCHAR(300) DEFAULT NULL,
        master_playlist VARCHAR(300) NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS SongInfo (
        song_id BIGINT PRIMARY KEY,
        release_year INT NOT NULL,
        composer VARCHAR(200) DEFAULT NULL,
        popularity BIGINT UNSIGNED NOT NULL DEFAULT 0,
        CONSTRAINT fk_songinfo_song FOREIGN KEY (song_id) REFERENCES SONG (id) ON DELETE CASCADE
    );

CREATE TABLE
    IF NOT EXISTS Artist (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        `name` VARCHAR(100) UNIQUE NOT NULL,
        cover_image VARCHAR(200) DEFAULT NULL,
        followers BIGINT UNSIGNED NOT NULL DEFAULT 0
    );

CREATE TABLE
    IF NOT EXISTS ArtistInfo (
        artist_id BIGINT PRIMARY KEY,
        biography TEXT DEFAULT NULL,
        birth_date DATE DEFAULT NULL,
        monthly_listeners BIGINT UNSIGNED DEFAULT 0,
        CONSTRAINT fk_artistinfo_artist FOREIGN KEY (artist_id) REFERENCES Artist (id) ON DELETE CASCADE
    );

CREATE TABLE
    IF NOT EXISTS Album (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        `name` VARCHAR(100) UNIQUE NOT NULL,
        popularity BIGINT UNSIGNED NOT NULL DEFAULT 0
    );

CREATE TABLE
    IF NOT EXISTS Genre (
        id INT PRIMARY KEY AUTO_INCREMENT,
        `name` VARCHAR(60) UNIQUE NOT NULL,
        cover_image VARCHAR(200) DEFAULT NULL,
        popularity BIGINT UNSIGNED NOT NULL DEFAULT 0
    );

CREATE TABLE
    IF NOT EXISTS SongCountry (
        song_id BIGINT,
        country_id INT,
        PRIMARY KEY (song_id, country_id),
        CONSTRAINT fk_songcountry_song FOREIGN KEY (song_id) REFERENCES Song (id) ON DELETE CASCADE,
        CONSTRAINT fk_songcountry_country FOREIGN KEY (country_id) REFERENCES Country (id) ON DELETE CASCADE
    );

CREATE TABLE
    IF NOT EXISTS SongArtist (
        song_id BIGINT,
        artist_id BIGINT,
        PRIMARY KEY (song_id, artist_id),
        CONSTRAINT fk_songartist_song FOREIGN KEY (song_id) REFERENCES Song (id) ON DELETE CASCADE,
        CONSTRAINT fk_songartist_artist FOREIGN KEY (artist_id) REFERENCES Artist (id) ON DELETE CASCADE
    );

CREATE TABLE
    IF NOT EXISTS SongAlbum (
        song_id BIGINT,
        album_id BIGINT,
        PRIMARY KEY (song_id, album_id),
        CONSTRAINT fk_songalbum_song FOREIGN KEY (song_id) REFERENCES Song (id) ON DELETE CASCADE,
        CONSTRAINT fk_songalbum_album FOREIGN KEY (album_id) REFERENCES Album (id) ON DELETE CASCADE
    );

CREATE TABLE
    IF NOT EXISTS SongGenre (
        song_id BIGINT,
        genre_id INT,
        PRIMARY KEY (song_id, genre_id),
        CONSTRAINT fk_songgenre_song FOREIGN KEY (song_id) REFERENCES Song (id) ON DELETE CASCADE,
        CONSTRAINT fk_songgenre_genre FOREIGN KEY (genre_id) REFERENCES Genre (id) ON DELETE CASCADE
    );

CREATE TABLE
    IF NOT EXISTS ArtistCountry (
        artist_id BIGINT,
        country_id INT,
        PRIMARY KEY (artist_id, country_id),
        CONSTRAINT fk_artistcountry_artist FOREIGN KEY (artist_id) REFERENCES Artist (id) ON DELETE CASCADE,
        CONSTRAINT fk_artistcountry_country FOREIGN KEY (country_id) REFERENCES Country (id) ON DELETE CASCADE
    );

CREATE TABLE
    IF NOT EXISTS ArtistAlbum (
        artist_id BIGINT,
        album_id BIGINT,
        PRIMARY KEY (artist_id, album_id),
        CONSTRAINT fk_artistalbum_artist FOREIGN KEY (artist_id) REFERENCES Artist (id) ON DELETE CASCADE,
        CONSTRAINT fk_artistalbum_album FOREIGN KEY (album_id) REFERENCES Album (id) ON DELETE CASCADE
    );

CREATE TABLE
    IF NOT EXISTS ArtistGenre (
        artist_id BIGINT,
        genre_id INT,
        PRIMARY KEY (artist_id, genre_id),
        CONSTRAINT fk_artistgenre_artist FOREIGN KEY (artist_id) REFERENCES Artist (id) ON DELETE CASCADE,
        CONSTRAINT fk_artistgenre_genre FOREIGN KEY (genre_id) REFERENCES Genre (id) ON DELETE CASCADE
    );