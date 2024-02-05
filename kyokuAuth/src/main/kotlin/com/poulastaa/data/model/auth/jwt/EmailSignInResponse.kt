package com.poulastaa.data.model.auth.jwt

import com.poulastaa.data.model.User
import com.poulastaa.data.model.auth.UserCreationStatus
import kotlinx.serialization.Serializable

@Serializable
data class EmailSignInResponse(
    val status: UserCreationStatus,
    val accessToken: String = "",
    val refreshToken: String = "",
    val user: User = User()
)
