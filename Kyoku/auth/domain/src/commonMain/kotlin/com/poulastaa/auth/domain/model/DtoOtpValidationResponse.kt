package com.poulastaa.auth.domain.model

data class DtoOtpValidationResponse(
    val status: DtoOtpValidationStatus,
    val token: String,
)

enum class DtoOtpValidationStatus {
    VALID,
    USER_NOT_FOUND,
    INVALID_CODE,
    INVALID_EMAIL,
    EXPIRED,
}
