package com.poulastaa.auth.network.req

import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthReq(
    val type: String = "com.poulastaa.data.model.auth.req.GoogleAuthReq",
    val token: String,
    val countryCode: String,
)