package com.poulastaa.auth.network.model

import com.poulastaa.auth.domain.model.DtoAuthResponse
import com.poulastaa.common.network.model.ResponseTokens
import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthResponse(
    val user: AuthUserResponse,
    val token: ResponseTokens,
) {
    fun toDto() = DtoAuthResponse(
        user = user.toDto(),
        tokens = token.toDto(),
    )
}
