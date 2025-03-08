package com.poulastaa.core.domain.repository

open class RedisKeys {
    private enum class ExpTime(val time: Long) {
        FIFTEEN(time = 15 * 60),
        TWENTY(time = 20 * 60)
    }

    protected enum class Group(val expTime: Long) {
        USER(expTime = ExpTime.FIFTEEN.time),

        SONG(expTime = ExpTime.FIFTEEN.time),
        SONG_TITLE(expTime = ExpTime.FIFTEEN.time),
        SONG_INFO(expTime = ExpTime.FIFTEEN.time),
        PREV_SONG(expTime = ExpTime.FIFTEEN.time),
        PREV_DETAILED_SONG(expTime = ExpTime.FIFTEEN.time),
        GENRE(expTime = ExpTime.FIFTEEN.time),
        GENRE_TITLE(expTime = ExpTime.FIFTEEN.time),
        ALBUM(expTime = ExpTime.FIFTEEN.time),
        ARTIST(expTime = ExpTime.FIFTEEN.time),
        PREV_ARTIST(expTime = ExpTime.FIFTEEN.time),
        PREV_ARTIST_TITLE(expTime = ExpTime.FIFTEEN.time),
        COUNTRY(expTime = ExpTime.FIFTEEN.time),
        PLAYLIST(expTime = ExpTime.TWENTY.time),
        COUNTRY_ID(expTime = 1 * 60 * 60),

        EMAIL_VERIFICATION_STATUS(expTime = ExpTime.TWENTY.time),
        JWT_TOKEN_STATUS(expTime = ExpTime.TWENTY.time),
        EMAIL_VERIFICATION_TOKEN(expTime = ExpTime.TWENTY.time),
        RESET_PASSWORD_TOKEN(expTime = ExpTime.TWENTY.time),

        RELATION_SONG_ALBUM(expTime = ExpTime.TWENTY.time),
        RELATION_SONG_GENRE(expTime = ExpTime.TWENTY.time),
        RELATION_SONG_ARTIST(expTime = ExpTime.TWENTY.time),
        RELATION_SONG_COUNTRY(expTime = ExpTime.TWENTY.time),

        RELATION_ARTIST_GENRE(expTime = ExpTime.TWENTY.time),
        RELATION_ARTIST_COUNTRY(expTime = ExpTime.TWENTY.time),

        RELATION_USER_PREV_GENRE(expTime = ExpTime.FIFTEEN.time)
    }

    protected enum class Channel(val delay: Long) {
        NOTIFICATION(delay = 10 * 1000)
    }
}