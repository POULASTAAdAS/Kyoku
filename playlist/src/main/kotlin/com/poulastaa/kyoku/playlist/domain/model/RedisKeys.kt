package com.poulastaa.kyoku.playlist.domain.model

open class RedisKeys {
    private enum class ExpTime(val time: Long) {
        DAY_1(time = 1L * 24 * 60 * 60),
        HOUR_12(time = 12L * 60 * 60),
        HOUR_6(time = 6L * 60 * 60),
    }

    protected enum class Group(
        val prefix: String? = null,
        val expTime: Long,
    ) {
        SONG_BY_ID(
            prefix = "SONG_BY_ID:",
            expTime = ExpTime.HOUR_6.time
        ),
        SONG_BY_TITLE(
            prefix = "SONG_BY_TITLE:",
            expTime = ExpTime.HOUR_6.time
        ),
    }
}
