package com.poulastaa.auth.data.model.res

import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserDto(
    val email: String = "",
    val userName: String = "",
    val profilePic: String = "",
)