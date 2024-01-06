package com.example.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class GoogleSignInResponse(
    val status: UserCreationStatus,
    val userName: String = "",
    val profilePic: String? = null,
    val token: String = "",
    val data: List<String> = emptyList(),
)
