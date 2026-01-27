package com.poulastaa.kyoku.content.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

open class RedisKeys {
    enum class ExpTime(val value: Duration) {
        MIN_10(value = 10.minutes),
        MIN_30(value = 30.minutes),
    }

    protected enum class Group(
        val prefix: String,
        val expTime: ExpTime,
    ) {
        GENRE(
            prefix = "GENRE",
            expTime = ExpTime.MIN_30
        ),
    }
}