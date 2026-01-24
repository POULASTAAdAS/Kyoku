package com.poulastaa.kyoku.playlist.domain.model

import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

open class RedisKeys {
    private enum class ExpTime(val time: Duration, val unit: TimeUnit) {
        DAY_1(time = 1.days, unit = TimeUnit.DAYS),
        HOUR_12(time = 12.hours, unit = TimeUnit.HOURS),
        HOUR_6(time = 6.hours, unit = TimeUnit.HOURS),
    }

    protected enum class Group(
        val prefix: String? = null,
        val expTime: Duration,
        val unit: TimeUnit,
    ) {
        SONG_BY_ID(
            prefix = "SONG_BY_ID:",
            expTime = ExpTime.HOUR_6.time,
            unit = ExpTime.HOUR_6.unit
        ),
        SONG_BY_TITLE(
            prefix = "SONG_BY_TITLE:",
            expTime = ExpTime.HOUR_6.time,
            unit = ExpTime.HOUR_6.unit
        ),
    }
}
