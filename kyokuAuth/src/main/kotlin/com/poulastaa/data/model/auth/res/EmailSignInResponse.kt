package com.poulastaa.data.model.auth.res

import com.poulastaa.data.model.auth.stat.UserCreationStatus
import kotlinx.serialization.Serializable

@Serializable
data class EmailSignInResponse(
    val status: UserCreationStatus,
    val userName: String = "",
    val accessToken: String = "",
    val refreshToken:String = "",
    val profilePic: String = ""
)
