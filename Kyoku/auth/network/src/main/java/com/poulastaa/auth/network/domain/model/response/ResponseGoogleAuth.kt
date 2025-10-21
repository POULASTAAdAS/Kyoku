package com.poulastaa.auth.network.domain.model.response

import com.poulastaa.core.network.domain.model.ResponseJWTToken
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGoogleAuth(
    val user: ResponseAuth,
    val token: ResponseJWTToken,
)
