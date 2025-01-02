package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.repository.auth.JWTRepository

open class RedisKeys {
    protected enum class Group(val expTime: Long) {
        USER(expTime = 15 * 60),

        SONG(expTime = 15 * 60),
        SONG_INFO(expTime = 15 * 60),
        GENRE(expTime = 7 * 60),
        ALBUM(expTime = 7 * 60),
        ARTIST(expTime = 7 * 60),
        COUNTRY(expTime = 7 * 60),
        PLAYLIST(expTime = 10 * 60),
        COUNTRY_ID(expTime = 1 * 60 * 60),

        EMAIL_VERIFICATION_STATUS(expTime = 10 * 60),
        JWT_TOKEN_STATUS(expTime = 10 * 60),
        EMAIL_VERIFICATION_TOKEN(expTime = JWTRepository.TokenType.TOKEN_VERIFICATION_MAIL.validationTime),
        RESET_PASSWORD_TOKEN(expTime = JWTRepository.TokenType.TOKEN_FORGOT_PASSWORD.validationTime),

        RELATION_SONG_ALBUM(expTime = 10 * 60),
        RELATION_SONG_GENRE(expTime = 10 * 60),
        RELATION_SONG_ARTIST(expTime = 10 * 60),
        RELATION_SONG_COUNTRY(expTime = 10 * 60),

        RELATION_ARTIST_GENRE(expTime = 10 * 60),
        RELATION_ARTIST_COUNTRY(expTime = 10 * 60),
    }

    protected enum class Channel(val delay: Long) {
        NOTIFICATION(delay = 1000 * 10)
    }
}