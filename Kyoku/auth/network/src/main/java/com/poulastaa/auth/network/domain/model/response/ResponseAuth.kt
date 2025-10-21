package com.poulastaa.auth.network.domain.model.response

import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Username
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAuth(
    val status: ResponseAuthResponseStatus,
    val email: Email,
    val username: Username,
    val profileUrl: String? = null,
    val type: ResponseUserType
)
