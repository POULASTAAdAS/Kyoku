package com.poulastaa.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: Long,
    val email: String,
)