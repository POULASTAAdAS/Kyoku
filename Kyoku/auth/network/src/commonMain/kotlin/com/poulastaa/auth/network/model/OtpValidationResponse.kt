package com.poulastaa.auth.network.model

import com.poulastaa.auth.domain.model.DtoOtpValidationResponse
import com.poulastaa.auth.domain.model.DtoOtpValidationStatus
import kotlinx.serialization.Serializable

@Serializable
data class OtpValidationResponse(
    val status: OtpValidationStatus = OtpValidationStatus.INVALID_CODE,
    val token: String = "",
) {
    fun toDto() = DtoOtpValidationResponse(
        status = status.toDto(),
        token = token,
    )
}

@Serializable
enum class OtpValidationStatus {
    VALID,
    USER_NOT_FOUND,
    INVALID_CODE,
    INVALID_EMAIL,
    EXPIRED;

    fun toDto() = when (this) {
        VALID -> DtoOtpValidationStatus.VALID
        USER_NOT_FOUND -> DtoOtpValidationStatus.USER_NOT_FOUND
        INVALID_CODE -> DtoOtpValidationStatus.INVALID_CODE
        INVALID_EMAIL -> DtoOtpValidationStatus.INVALID_EMAIL
        EXPIRED -> DtoOtpValidationStatus.EXPIRED
    }
}
