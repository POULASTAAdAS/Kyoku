package com.poulastaa.kyoku.auth.model.dto

data class DtoRateLimitResult(
    val consumed: Boolean,
    val remainingTokens: Long,
    val retryAfterSeconds: Long,
)
