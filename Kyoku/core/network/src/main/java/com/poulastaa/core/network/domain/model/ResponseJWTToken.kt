package com.poulastaa.core.network.domain.model

import com.poulastaa.core.domain.utils.JWTToken
import kotlinx.serialization.Serializable

@Serializable
data class ResponseJWTToken(
    val accessToken: JWTToken,
    val refreshToken: JWTToken,
)
