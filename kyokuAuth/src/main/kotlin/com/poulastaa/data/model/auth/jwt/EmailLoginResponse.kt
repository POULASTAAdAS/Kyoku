package com.poulastaa.data.model.auth.jwt

import com.poulastaa.data.model.User
import com.poulastaa.data.model.auth.auth_response.HomeResponse
import kotlinx.serialization.Serializable

@Serializable
data class EmailLoginResponse(
    val status: EmailLoginStatus,
    val accessToken: String = "",
    val refreshToken: String = "",
    val user: User = User(),
    val data: HomeResponse = HomeResponse()
)