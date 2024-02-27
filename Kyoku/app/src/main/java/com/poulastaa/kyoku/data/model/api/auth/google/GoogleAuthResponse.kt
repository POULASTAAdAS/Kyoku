package com.poulastaa.kyoku.data.model.api.auth.google

import com.poulastaa.kyoku.data.model.User
import com.poulastaa.kyoku.data.model.api.auth.UserCreationStatus
import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthResponse(
    val status: UserCreationStatus,
    val user: User,
    val data: List<String> = emptyList(),
)
