package com.example.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class EmailSignInResponse(
    val userName: String = "",
    val token: String = "",
    val status: UserCreationStatus,
    val profilePic: String? = null
)
