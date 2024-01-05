package com.example.data.model

import com.example.routes.auth.common.UserCreationStatus
import kotlinx.serialization.Serializable

@Serializable
data class UserCreationResponse(
    val status: UserCreationStatus
)
