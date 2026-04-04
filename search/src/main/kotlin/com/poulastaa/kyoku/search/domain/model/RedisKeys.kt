package com.poulastaa.kyoku.search.domain.model

import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

open class RedisKeys {
    protected enum class ExpTime(val time: Duration, val unit: TimeUnit) {
        MIN_15(15.minutes, TimeUnit.MINUTES)
    }

    protected enum class Group(
        protected val prefix: String,
        val expTime: ExpTime,
    ) {
        POPULAR_ARTIST_BY_COUNTRY(
            prefix = "ARTIST",
            expTime = ExpTime.MIN_15,
        ) {
            override fun buildKey(payload: String?) = payload?.let { "$prefix:$it" } ?: prefix
        };

        abstract fun buildKey(payload: String? = null): String
    }
}