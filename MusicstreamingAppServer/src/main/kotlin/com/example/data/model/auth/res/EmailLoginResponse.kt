package com.example.data.model.auth.res

import com.example.data.model.auth.stat.EmailLoginStatus
import kotlinx.serialization.Serializable

@Serializable
data class EmailLoginResponse(
    val status: EmailLoginStatus,
    val userName: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
    val profilePic: String = "",
    val data: List<String> = emptyList(), // todo add
)
