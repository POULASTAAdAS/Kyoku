package com.poulastaa.auth.network.mapper

import com.poulastaa.auth.domain.model.*
import com.poulastaa.auth.network.model.*
import com.poulastaa.core.domain.model.UserDto

fun UpdatePasswordStatusDto.toUpdatePasswordStatus() = when (this) {
    UpdatePasswordStatusDto.RESET -> UpdatePasswordStatus.RESET
    UpdatePasswordStatusDto.SAME_PASSWORD -> UpdatePasswordStatus.SAME_PASSWORD
    UpdatePasswordStatusDto.USER_NOT_FOUND -> UpdatePasswordStatus.USER_NOT_FOUND
    UpdatePasswordStatusDto.TOKEN_USED -> UpdatePasswordStatus.TOKEN_USED
    UpdatePasswordStatusDto.SERVER_ERROR -> UpdatePasswordStatus.SERVER_ERROR
}

fun ForgotPasswordResponseStatusDto.toForgotPasswordResponse() = ForgotPasswordResponse(
    status = when (this) {
        ForgotPasswordResponseStatusDto.FORGOT_PASSWORD_MAIL_SEND -> ForgotPasswordResponseStatus.FORGOT_PASSWORD_MAIL_SEND
        ForgotPasswordResponseStatusDto.USER_NOT_FOUND -> ForgotPasswordResponseStatus.USER_NOT_FOUND
        ForgotPasswordResponseStatusDto.SERVER_ERROR -> ForgotPasswordResponseStatus.SERVER_ERROR
        ForgotPasswordResponseStatusDto.EMAIL_NOT_PROVIDED -> ForgotPasswordResponseStatus.EMAIL_NOT_PROVIDED
        ForgotPasswordResponseStatusDto.EMAIL_NOT_VALID -> ForgotPasswordResponseStatus.EMAIL_NOT_VALID
    }
)

fun JwtTokenDto.toJWTResponse() = JwtTokenResponse(
    state = true,
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
)

fun AuthResponseDto.toAuthResponse() = AuthenticationResponse(
    status = this.status.toAuthenticationResponseStatus(),
    user = this.user.toResponseUser(),
)

private fun AuthResponseStatusDto.toAuthenticationResponseStatus() = when (this) {
    AuthResponseStatusDto.USER_CREATED -> AuthStatusResponse.CREATED
    AuthResponseStatusDto.USER_FOUND -> AuthStatusResponse.USER_FOUND
    AuthResponseStatusDto.USER_FOUND_STORE_B_DATE -> AuthStatusResponse.USER_FOUND_STORE_B_DATE
    AuthResponseStatusDto.USER_FOUND_SET_GENRE -> AuthStatusResponse.USER_FOUND_SET_GENRE
    AuthResponseStatusDto.USER_FOUND_SET_ARTIST -> AuthStatusResponse.USER_FOUND_SET_ARTIST
    AuthResponseStatusDto.USER_FOUND_HOME -> AuthStatusResponse.USER_FOUND_HOME
    AuthResponseStatusDto.EMAIL_NOT_VERIFIED -> AuthStatusResponse.EMAIL_NOT_VERIFIED
    AuthResponseStatusDto.EMAIL_ALREADY_IN_USE -> AuthStatusResponse.EMAIL_ALREADY_IN_USE
    AuthResponseStatusDto.INVALID_EMAIL -> AuthStatusResponse.INVALID_EMAIL
    AuthResponseStatusDto.PASSWORD_DOES_NOT_MATCH -> AuthStatusResponse.PASSWORD_DOES_NOT_MATCH
    AuthResponseStatusDto.USER_NOT_FOUND -> AuthStatusResponse.USER_NOT_FOUND
    AuthResponseStatusDto.TOKEN_EXPIRED -> AuthStatusResponse.TOKEN_EXPIRED
    AuthResponseStatusDto.SERVER_ERROR -> AuthStatusResponse.SERVER_ERROR
}

private fun UserDto.toResponseUser() = ResponseUser(
    email = this.email,
    username = this.username,
    profilePicUrl = this.profilePicUrl
)

fun EmailSignUpRequest.toEmailSignUpPayload() = EmailSignUpPayload(
    email = this.email,
    password = this.password,
    username = this.username,
    countryCode = this.countryCode,
)

fun EmailLogInRequest.toEmailSignInPayload() = EmailLogInPayload(
    email = this.email,
    password = this.password,
)