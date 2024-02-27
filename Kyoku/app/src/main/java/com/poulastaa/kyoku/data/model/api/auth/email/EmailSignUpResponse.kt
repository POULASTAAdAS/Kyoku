package com.poulastaa.kyoku.data.model.api.auth.email

import com.poulastaa.kyoku.data.model.User
import com.poulastaa.kyoku.data.model.api.auth.UserCreationStatus
import kotlinx.serialization.Serializable

@Serializable
data class EmailSignUpResponse(
    val status: UserCreationStatus,
    val accessToken: String = "",
    val refreshToken: String = "",
    val user: User
)
