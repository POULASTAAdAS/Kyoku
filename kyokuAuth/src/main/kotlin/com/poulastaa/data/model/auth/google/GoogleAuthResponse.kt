package com.poulastaa.data.model.auth.google

import com.poulastaa.data.model.User
import com.poulastaa.data.model.auth.UserCreationStatus
import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthResponse(
    val status: UserCreationStatus,
    val user: User = User(),
    val data: List<String> = emptyList(),
)
