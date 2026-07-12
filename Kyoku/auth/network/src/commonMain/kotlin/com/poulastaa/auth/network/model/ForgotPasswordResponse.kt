package com.poulastaa.auth.network.model

import com.poulastaa.auth.domain.model.DtoForgotPasswordResponse
import com.poulastaa.auth.domain.model.DtoForgotPasswordStatus
import kotlinx.serialization.Serializable

@Serializable
enum class ForgotPasswordResponse {
    SENT,
    USER_NOT_FOUND,
    INVALID_EMAIL,
    ERROR;

    fun toDto() = DtoForgotPasswordResponse(
        status = when (this) {
            SENT -> DtoForgotPasswordStatus.SENT
            USER_NOT_FOUND -> DtoForgotPasswordStatus.USER_NOT_FOUND
            INVALID_EMAIL -> DtoForgotPasswordStatus.INVALID_EMAIL
            ERROR -> DtoForgotPasswordStatus.ERROR
        }
    )
}
