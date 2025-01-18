package com.poulastaa.auth.network

import com.poulastaa.auth.domain.model.AuthResponseDto
import com.poulastaa.auth.domain.model.AuthStatus
import com.poulastaa.auth.domain.model.ForgotPasswordStatus
import com.poulastaa.auth.domain.model.JwtTokenDto
import com.poulastaa.auth.network.model.AuthStatusResponse
import com.poulastaa.auth.network.model.AuthenticationResponse
import com.poulastaa.auth.network.model.ForgotPasswordResponseStatus
import com.poulastaa.auth.network.model.JwtTokenResponse
import com.poulastaa.auth.network.model.ResponseUser
import com.poulastaa.core.domain.model.DtoUser

fun AuthenticationResponse.toAuthResponseDto() = AuthResponseDto(
    status = this.status.toAuthStatus(),
    user = user.toUserDto(),
)

fun ResponseUser.toUserDto() = DtoUser(
    email = this.email,
    name = this.username,
    profilePic = this.profilePicUrl ?: ""
)

fun AuthStatusResponse.toAuthStatus() = when (this) {
    AuthStatusResponse.CREATED -> AuthStatus.CREATED
    AuthStatusResponse.USER_FOUND -> AuthStatus.USER_FOUND
    AuthStatusResponse.USER_FOUND_STORE_B_DATE -> AuthStatus.USER_FOUND_STORE_B_DATE
    AuthStatusResponse.USER_FOUND_SET_GENRE -> AuthStatus.USER_FOUND_SET_GENRE
    AuthStatusResponse.USER_FOUND_SET_ARTIST -> AuthStatus.USER_FOUND_SET_ARTIST
    AuthStatusResponse.USER_FOUND_HOME -> AuthStatus.USER_FOUND_HOME
    AuthStatusResponse.EMAIL_NOT_VERIFIED -> AuthStatus.EMAIL_NOT_VERIFIED
    AuthStatusResponse.EMAIL_ALREADY_IN_USE -> AuthStatus.EMAIL_ALREADY_IN_USE
    AuthStatusResponse.INVALID_EMAIL -> AuthStatus.INVALID_EMAIL
    AuthStatusResponse.PASSWORD_DOES_NOT_MATCH -> AuthStatus.PASSWORD_DOES_NOT_MATCH
    AuthStatusResponse.USER_NOT_FOUND -> AuthStatus.USER_NOT_FOUND
    AuthStatusResponse.TOKEN_EXPIRED -> AuthStatus.TOKEN_EXPIRED
    AuthStatusResponse.SERVER_ERROR -> AuthStatus.SERVER_ERROR
}

fun JwtTokenResponse.toJWTTokenDto() = JwtTokenDto(
    status = this.state,
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)

fun ForgotPasswordResponseStatus.toForgotPasswordStatus() = when (this) {
    ForgotPasswordResponseStatus.FORGOT_PASSWORD_MAIL_SEND -> ForgotPasswordStatus.FORGOT_PASSWORD_MAIL_SEND
    ForgotPasswordResponseStatus.USER_NOT_FOUND -> ForgotPasswordStatus.USER_NOT_FOUND
    ForgotPasswordResponseStatus.SERVER_ERROR -> ForgotPasswordStatus.SERVER_ERROR
    ForgotPasswordResponseStatus.EMAIL_NOT_PROVIDED -> ForgotPasswordStatus.EMAIL_NOT_PROVIDED
    ForgotPasswordResponseStatus.EMAIL_NOT_VALID -> ForgotPasswordStatus.EMAIL_NOT_VALID
}