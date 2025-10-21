package com.poulastaa.auth.network.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
enum class CodeValidationResponseStatus {
    VALID,
    USER_NOT_FOUND,
    INVALID_CODE,
    INVALID_EMAIL,
    EXPIRED
}