package com.poulastaa.data.model.auth.response

import com.poulastaa.data.model.User
import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthRes(
    val status: UserAuthStatus = UserAuthStatus.USER_NOT_FOUND,
    val user: User = User(),
    // todo
)
