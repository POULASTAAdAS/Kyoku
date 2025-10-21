package com.poulastaa.auth.network.domain.mapper

import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.auth.domain.model.DtoResetPasswordState
import com.poulastaa.auth.domain.model.DtoResponseUser
import com.poulastaa.auth.domain.model.DtoValidateOTPPayload
import com.poulastaa.auth.domain.model.DtoValidateOTPStatus
import com.poulastaa.auth.network.domain.model.response.CodeValidationResponseStatus
import com.poulastaa.auth.network.domain.model.response.ResponseAuthResponseStatus
import com.poulastaa.auth.network.domain.model.response.ResponseEmailAuth
import com.poulastaa.auth.network.domain.model.response.ResponseUserType
import com.poulastaa.auth.network.domain.model.response.ResponseValidateOTP
import com.poulastaa.auth.network.domain.model.response.UpdatePasswordResponse
import com.poulastaa.auth.network.domain.model.response.UpdatePasswordStatus
import com.poulastaa.core.domain.model.DtoUserType

internal fun ResponseEmailAuth.toDtoResponseUser(): DtoResponseUser = DtoResponseUser(
    status = when (this.status) {
        ResponseAuthResponseStatus.USER_CREATED -> DtoAuthResponseStatus.USER_CREATED
        ResponseAuthResponseStatus.USER_FOUND -> DtoAuthResponseStatus.USER_FOUND
        ResponseAuthResponseStatus.USER_FOUND_NO_PLAYLIST -> DtoAuthResponseStatus.USER_FOUND_NO_PLAYLIST
        ResponseAuthResponseStatus.USER_FOUND_NO_ARTIST -> DtoAuthResponseStatus.USER_FOUND_NO_ARTIST
        ResponseAuthResponseStatus.USER_FOUND_NO_GENRE -> DtoAuthResponseStatus.USER_FOUND_NO_GENRE
        ResponseAuthResponseStatus.USER_FOUND_NO_B_DATE -> DtoAuthResponseStatus.USER_FOUND_NO_B_DATE
        ResponseAuthResponseStatus.EMAIL_NOT_VALID -> DtoAuthResponseStatus.EMAIL_NOT_VALID
        ResponseAuthResponseStatus.PASSWORD_DOES_NOT_MATCH -> DtoAuthResponseStatus.PASSWORD_DOES_NOT_MATCH
        ResponseAuthResponseStatus.USER_NOT_FOUND -> DtoAuthResponseStatus.USER_NOT_FOUND
        ResponseAuthResponseStatus.UNAUTHORIZED -> DtoAuthResponseStatus.UNAUTHORIZED
        ResponseAuthResponseStatus.INTERNAL_SERVER_ERROR -> DtoAuthResponseStatus.INTERNAL_SERVER_ERROR
        ResponseAuthResponseStatus.EMAIL_ALREADY_IN_USE -> DtoAuthResponseStatus.EMAIL_ALREADY_IN_USE
    },
    email = this.email,
    username = this.username,
    profileUrl = this.profileUrl,
    type = when (this.type) {
        ResponseUserType.EMAIL -> DtoUserType.EMAIL
        ResponseUserType.GOOGLE -> DtoUserType.GOOGLE
        ResponseUserType.DEFAULT -> DtoUserType.DEFAULT
    }
)

internal fun ResponseValidateOTP.toDtoValidationOTPPayload() = DtoValidateOTPPayload(
    status = when (this.status) {
        CodeValidationResponseStatus.VALID -> DtoValidateOTPStatus.VALID
        CodeValidationResponseStatus.USER_NOT_FOUND -> DtoValidateOTPStatus.USER_NOT_FOUND
        CodeValidationResponseStatus.INVALID_CODE -> DtoValidateOTPStatus.INVALID_CODE
        CodeValidationResponseStatus.INVALID_EMAIL -> DtoValidateOTPStatus.INVALID_EMAIL
        CodeValidationResponseStatus.EXPIRED -> DtoValidateOTPStatus.EXPIRED
    },
    token = this.token
)

internal fun UpdatePasswordResponse.toDtoUpdatePasswordStatus() = when (this.status) {
    UpdatePasswordStatus.UPDATED -> DtoResetPasswordState.UPDATED
    UpdatePasswordStatus.USER_NOT_FOUND -> DtoResetPasswordState.USER_NOT_FOUND
    UpdatePasswordStatus.SAME_PASSWORD -> DtoResetPasswordState.SAME_PASSWORD
    UpdatePasswordStatus.INVALID_PASSWORD -> DtoResetPasswordState.INVALID_PASSWORD
    UpdatePasswordStatus.EXPIRED_TOKEN -> DtoResetPasswordState.EXPIRED_TOKEN
    UpdatePasswordStatus.ERROR -> DtoResetPasswordState.ERROR
    UpdatePasswordStatus.INVALID_TOKEN -> DtoResetPasswordState.INVALID_TOKEN
}