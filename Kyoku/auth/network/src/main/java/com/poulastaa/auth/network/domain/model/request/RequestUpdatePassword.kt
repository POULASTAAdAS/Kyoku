package com.poulastaa.auth.network.domain.model.request

import com.poulastaa.core.domain.utils.JWTToken
import com.poulastaa.core.domain.utils.Password
import kotlinx.serialization.Serializable

@Serializable
data class RequestUpdatePassword(
    val password: Password,
    val token: JWTToken,
)
