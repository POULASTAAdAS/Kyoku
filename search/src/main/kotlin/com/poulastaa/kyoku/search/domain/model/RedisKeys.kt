package com.poulastaa.kyoku.search.domain.model

import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

open class RedisKeys {
    protected enum class ExpTime(val time: Duration, val unit: TimeUnit) {
        MIN_15(15.minutes, TimeUnit.MINUTES),
        HOUR_6(6.hours, TimeUnit.HOURS),
    }

    protected enum class Group(
        protected val prefix: String,
        val expTime: ExpTime,
    ) {
        POPULAR_ARTIST_BY_COUNTRY(
            prefix = "ARTIST",
            expTime = ExpTime.MIN_15,
        ),
        COUNTRY_BY_NAME(
            prefix = "COUNTRY",
            expTime = ExpTime.HOUR_6,
        );

        fun buildKey(payload: String? = null) = payload?.let { "$prefix:$it" } ?: prefix
    }
}