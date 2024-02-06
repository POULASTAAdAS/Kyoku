package com.poulastaa.kyoku.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userName: String = "",
    val profilePic: String = ""
)
