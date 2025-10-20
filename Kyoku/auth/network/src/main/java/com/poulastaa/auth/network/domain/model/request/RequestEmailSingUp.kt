package com.poulastaa.auth.network.domain.model.request

import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.utils.Username
import kotlinx.serialization.Serializable

@Serializable
data class RequestEmailSingUp(
    val email: Email,
    val username: Username,
    val password: Password,
    val countryCode: String,
)
