package com.pouluastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthRequest(
    val token: String,
    val countryCode: String,
)
