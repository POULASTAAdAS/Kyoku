package com.poulastaa.data.model.auth.passkey

import com.poulastaa.data.model.User
import com.poulastaa.data.model.auth.UserCreationStatus
import kotlinx.serialization.Serializable

@Serializable
data class PasskeyAuthResponse(
    val status: UserCreationStatus,
    val user: User = User(),
    val data: List<String> = emptyList() // todo change
)
