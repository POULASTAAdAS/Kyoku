package com.poulastaa.kyoku.data.model.api.auth.email

import com.poulastaa.kyoku.data.model.User
import kotlinx.serialization.Serializable

@Serializable
data class EmailLogInResponse(
    val status: EmailLoginStatus,
    val accessToken: String = "",
    val refreshToken: String = "",
    val user: User,
    val data: List<String>
)
