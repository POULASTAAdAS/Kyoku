package com.poulastaa.core.domain.repository

open class RedisKeys {
    protected object Group {
        const val USER = "USER"
        const val SONG = "SONG"
        const val COUNTRY_ID = "COUNTRY_ID"
        const val EMAIL_VERIFICATION_STATUS = "EMAIL_VERIFICATION_STATUS"
        const val JWT_TOKEN_STATUS = "JWT_TOKEN_STATUS"
        const val EMAIL_VERIFICATION_TOKEN = "EMAIL_VERIFICATION_TOKEN"
        const val RESET_PASSWORD_TOKEN = "RESET_PASSWORD_TOKEN"
        const val PLAYLIST = "PLAYLIST"
    }

    protected object Channel {
        const val NOTIFICATION = "NOTIFICATION"
    }
}