package com.example.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserCreationResponse(
    val status: UserCreationStatus
)
