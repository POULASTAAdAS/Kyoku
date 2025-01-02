package com.poulastaa.core.domain.repository

open class RedisKeys {
    protected enum class Group(val expTime: Long) {
        USER(expTime = 15 * 60),

        SONG(expTime = 15 * 60),
        SONG_TITLE(expTime = 15 * 60),
        SONG_INFO(expTime = 15 * 60),
        GENRE(expTime = 15 * 60),
        ALBUM(expTime = 15 * 60),
        ARTIST(expTime = 15 * 60),
        COUNTRY(expTime = 15 * 60),
        PLAYLIST(expTime = 20 * 60),
        COUNTRY_ID(expTime = 1 * 60 * 60),

        EMAIL_VERIFICATION_STATUS(expTime = 20 * 60),
        JWT_TOKEN_STATUS(expTime = 20 * 60),
        EMAIL_VERIFICATION_TOKEN(expTime = 20 * 60),
        RESET_PASSWORD_TOKEN(expTime = 20 * 60),

        RELATION_SONG_ALBUM(expTime = 20 * 60),
        RELATION_SONG_GENRE(expTime = 20 * 60),
        RELATION_SONG_ARTIST(expTime = 20 * 60),
        RELATION_SONG_COUNTRY(expTime = 20 * 60),

        RELATION_ARTIST_GENRE(expTime = 20 * 60),
        RELATION_ARTIST_COUNTRY(expTime = 20 * 60),
    }

    protected enum class Channel(val delay: Long) {
        NOTIFICATION(delay = 1000 * 10)
    }
}