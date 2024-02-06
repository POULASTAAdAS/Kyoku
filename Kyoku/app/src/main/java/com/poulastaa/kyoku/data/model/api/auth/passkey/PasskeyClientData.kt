package com.poulastaa.kyoku.data.model.api.auth.passkey

import kotlinx.serialization.Serializable

@Serializable
data class PasskeyClientData(
    val type: String,
    val challenge: String,
    val origin: String,
    val androidPackageName: String
)
