package com.pouluastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseUser(
    val email: String = "",
    val username: String = "",
    val profilePicUrl: String? = null,
)
