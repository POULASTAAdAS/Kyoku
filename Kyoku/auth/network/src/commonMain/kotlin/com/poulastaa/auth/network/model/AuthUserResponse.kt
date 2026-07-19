package com.poulastaa.auth.network.model

import com.poulastaa.common.domain.model.DtoUser
import kotlinx.serialization.Serializable

@Serializable
data class AuthUserResponse(
    val userId: Long,
    val status: String,
    val email: String,
    val username: String,
    val profileUrl: String? = null,
    val type: String,
) {
    fun toDto(isNewUser: Boolean = status == USER_CREATED) = DtoUser(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profileUrl,
        isNewUser = isNewUser,
    )

    private companion object {
        const val USER_CREATED = "USER_CREATED"
    }
}
