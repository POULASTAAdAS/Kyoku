package com.poulastaa.auth.network.domain.model.response

import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Username
import kotlinx.serialization.Serializable

@Serializable
data class ResponseEmailLogIn(
    val status: ResponseAuthResponseStatus,
    val email: Email,
    val username: Username,
    val profileUrl: String? = null,
    val type: ResponseUserType
)
