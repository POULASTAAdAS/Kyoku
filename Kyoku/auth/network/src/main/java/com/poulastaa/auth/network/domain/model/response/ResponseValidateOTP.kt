package com.poulastaa.auth.network.domain.model.response

import com.poulastaa.core.domain.utils.JWTToken
import kotlinx.serialization.Serializable

@Serializable
data class ResponseValidateOTP(
    val status: CodeValidationResponseStatus,
    val token: JWTToken,
)
