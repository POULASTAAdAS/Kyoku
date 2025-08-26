CREATE DATABASE IF NOT EXISTS ACTIVITY;

USE ACTIVITY;

CREATE TABLE
    IF NOT EXISTS UserFollowedArtist (
        user_id BIGINT NOT NULL,
        artist_id BIGINT NOT NULL,
        PRIMARY KEY (user_id, artist_id)
    );

CREATE TABLE
    IF NOT EXISTS UserSavedAlbum (
        user_id BIGINT NOT NULL,
        album_id BIGINT NOT NULL,
        PRIMARY KEY (user_id, album_id)
    );

CREATE TABLE
    IF NOT EXISTS UserFollowedGenre (
        user_id BIGINT NOT NULL,
        genre_id INT NOT NULL,
        PRIMARY KEY (user_id, genre_id)
    );

CREATE TABLE
    IF NOT EXISTS UserFavouriteSong (
        user_id BIGINT NOT NULL,
        song_id BIGINT NOT NULL,
        PRIMARY KEY (user_id, song_id)
    );

CREATE TABLE
    IF NOT EXISTS UserPinnedType (
        id INT PRIMARY KEY AUTO_INCREMENT,
        `type` VARCHAR(10) NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS UserPinned (
        user_id BIGINT NOT NULL,
        other_id BIGINT NOT NULL,
        other_type_id INT NOT NULL,
        PRIMARY KEY (user_id, other_id, other_type_id),
        CONSTRAINT fk_userpinned_type FOREIGN KEY (other_type_id) REFERENCES UserPinnedType (id) ON DELETE CASCADE
    );