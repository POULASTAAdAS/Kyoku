package com.poulastaa.profile.netwokr.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUsernameReq(
    val username: String,
)
