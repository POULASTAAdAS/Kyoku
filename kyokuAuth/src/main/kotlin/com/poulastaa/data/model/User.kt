package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userName: String = "",
    val profilePic: String = ""
)
