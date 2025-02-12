package com.poulastaa.auth.domain.model

data class PasskeyPayload(
    val type: PasskeyType,
    val challenge: String,
    val json: String,
)
