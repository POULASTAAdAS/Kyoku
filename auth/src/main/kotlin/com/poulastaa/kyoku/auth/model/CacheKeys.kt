package com.poulastaa.kyoku.auth.model

import java.util.concurrent.TimeUnit

open class CacheKeys {
    private enum class ExpTime(
        val time: Long,
        val unit: TimeUnit = TimeUnit.MINUTES,
    ) {
        TEN(time = 10),
        FIFTEEN(time = 15),
        TWENTY(time = 20)
    }

    protected enum class Group(
        val prefix: String? = null,
        val expTime: Long = ExpTime.TEN.time,
        val unit: TimeUnit = ExpTime.TEN.unit,
    ) {
        VERIFICATION_TOKEN,
        EMAIL_VERIFICATION_STATUS,
        USER(expTime = ExpTime.FIFTEEN.time),
        PASSWORD_RESET_STATUS(expTime = ExpTime.FIFTEEN.time),
    }
}