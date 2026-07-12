package com.poulastaa.auth.network.model

import com.poulastaa.auth.domain.model.DtoAuthResponse
import com.poulastaa.common.domain.model.DtoUser
import com.poulastaa.common.network.model.ResponseTokens
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthRequest(
    val token: String,
    val code: String,
)

@Serializable
data class GoogleAuthResponse(
    val user: GoogleAuthUser,
    val token: ResponseTokens,
) {
    fun toDto() = DtoAuthResponse(
        user = user.toDto(),
        tokens = token.toDto(),
    )
}

@Serializable
data class GoogleAuthUser(
    val userId: Long = -1,
    val status: String = "",
    val email: String = "",
    val username: String = "",
    @SerialName("profileUrl")
    val profilePictureUrl: String? = null,
) {
    fun toDto() = DtoUser(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profilePictureUrl,
        isNewUser = status == "USER_CREATED",
    )
}
