CREATE DATABASE IF NOT EXISTS PLAYLIST;

USE PLAYLIST;

CREATE TABLE
    IF NOT EXISTS Playlist (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        `name` varchar(120) NOT NULL,
        `description` TEXT DEFAULT NULL,
        visibility_state BOOL DEFAULT FALSE,
        popularity BIGINT UNSIGNED NOT NULL DEFAULT 0,
        song_count INT UNSIGNED DEFAULT 0,
        total_duration INT UNSIGNED NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        INDEX idx_playlist_name (`name`)
    );

CREATE TABLE
    SongPlaylist (
        song_id BIGINT NOT NULL,
        playlist_id BIGINT NOT NULL,
        PRIMARY KEY (song_id, playlist_id),
        CONSTRAINT fk_song_playlist FOREIGN KEY (playlist_id) REFERENCES Playlist (id) ON DELETE CASCADE
    );

CREATE TABLE
    IF NOT EXISTS UserPlaylist (
        user_id BIGINT NOT NULL,
        playlist_id BIGINT NOT NULL,
        PRIMARY KEY (user_id, playlist_id),
        CONSTRAINT fk_user_playlist FOREIGN KEY (playlist_id) REFERENCES Playlist (id) ON DELETE CASCADE
    );