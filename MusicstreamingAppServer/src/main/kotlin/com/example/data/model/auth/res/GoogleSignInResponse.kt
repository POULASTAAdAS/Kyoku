package com.example.data.model.auth.res

import com.example.data.model.auth.stat.UserCreationStatus
import kotlinx.serialization.Serializable

@Serializable
data class GoogleSignInResponse(
    val status: UserCreationStatus,
    val userName: String = "",
    val profilePic: String? = null,
    val data: List<String> = emptyList(),
)
