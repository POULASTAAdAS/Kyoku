package com.example.data.model.auth.res

import com.example.data.model.auth.stat.UserCreationStatus
import kotlinx.serialization.Serializable

@Serializable
data class EmailSignInResponse(
    val status: UserCreationStatus,
    val userName: String = "",
    val token: String = "",
    val profilePic: String = ""
)
