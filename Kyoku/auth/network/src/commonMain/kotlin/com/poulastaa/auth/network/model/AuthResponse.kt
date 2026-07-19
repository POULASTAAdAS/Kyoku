package com.poulastaa.auth.network.model

import com.poulastaa.auth.domain.model.DtoEmailAuthResponse
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val user: AuthUserResponse,
    val isNewUser: Boolean,
) {
    fun toDto() = DtoEmailAuthResponse(
        user = user.toDto(isNewUser),
        isNewUser = isNewUser,
    )
}
