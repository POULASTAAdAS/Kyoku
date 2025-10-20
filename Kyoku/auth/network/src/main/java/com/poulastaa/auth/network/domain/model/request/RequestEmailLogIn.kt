package com.poulastaa.auth.network.domain.model.request

import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import kotlinx.serialization.Serializable

@Serializable
data class RequestEmailLogIn(
    val email: Email,
    val password: Password
)
