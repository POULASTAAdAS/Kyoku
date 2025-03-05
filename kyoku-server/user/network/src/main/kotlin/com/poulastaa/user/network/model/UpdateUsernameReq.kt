package com.poulastaa.user.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUsernameReq(
    val username: String,
)