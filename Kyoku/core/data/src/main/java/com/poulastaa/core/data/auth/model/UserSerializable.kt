package com.poulastaa.core.data.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSerializable(
    val name: String,
    val email: String,
    val profilePic: String,
)
