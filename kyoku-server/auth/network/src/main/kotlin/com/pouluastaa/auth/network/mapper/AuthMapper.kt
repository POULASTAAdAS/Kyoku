package com.pouluastaa.auth.network.mapper

import com.poulastaa.core.domain.model.AuthResponseDto
import com.poulastaa.core.domain.model.AuthResponseStatusDto
import com.poulastaa.core.domain.model.JwtTokenDto
import com.poulastaa.core.domain.model.UserDto
import com.pouluastaa.auth.network.model.AuthenticationResponse
import com.pouluastaa.auth.network.model.AuthenticationResponseStatus
import com.pouluastaa.auth.network.model.JwtTokenResponse
import com.pouluastaa.auth.network.model.ResponseUser

fun AuthResponseDto.toAuthResponse() = AuthenticationResponse(
    status = this.status.toAuthenticationResponseStatus(),
    user = this.user.toResponseUser(),
    token = this.token.toJwtTokenResponse()
)

private fun AuthResponseStatusDto.toAuthenticationResponseStatus() = when (this) {
    AuthResponseStatusDto.USER_CREATED -> AuthenticationResponseStatus.CREATED
    AuthResponseStatusDto.USER_FOUND -> AuthenticationResponseStatus.USER_FOUND
    AuthResponseStatusDto.USER_FOUND_STORE_B_DATE -> AuthenticationResponseStatus.USER_FOUND_STORE_B_DATE
    AuthResponseStatusDto.USER_FOUND_SET_GENRE -> AuthenticationResponseStatus.USER_FOUND_SET_GENRE
    AuthResponseStatusDto.USER_FOUND_SET_ARTIST -> AuthenticationResponseStatus.USER_FOUND_SET_ARTIST
    AuthResponseStatusDto.USER_FOUND_HOME -> AuthenticationResponseStatus.USER_FOUND_HOME
    AuthResponseStatusDto.EMAIL_NOT_VERIFIED -> AuthenticationResponseStatus.EMAIL_NOT_VERIFIED
    AuthResponseStatusDto.EMAIL_ALREADY_IN_USE -> AuthenticationResponseStatus.EMAIL_ALREADY_IN_USE
    AuthResponseStatusDto.INVALID_EMAIL -> AuthenticationResponseStatus.INVALID_EMAIL
    AuthResponseStatusDto.PASSWORD_DOES_NOT_MATCH -> AuthenticationResponseStatus.PASSWORD_DOES_NOT_MATCH
    AuthResponseStatusDto.USER_NOT_FOUND -> AuthenticationResponseStatus.USER_NOT_FOUND
    AuthResponseStatusDto.TOKEN_EXPIRED -> AuthenticationResponseStatus.TOKEN_EXPIRED
    AuthResponseStatusDto.SERVER_ERROR -> AuthenticationResponseStatus.SERVER_ERROR
}

private fun UserDto.toResponseUser() = ResponseUser(
    email = this.email,
    username = this.username,
    profilePicUrl = this.profilePicUrl
)

private fun JwtTokenDto.toJwtTokenResponse() = JwtTokenResponse(
    refreshToken = this.refreshToken,
    accessToken = this.accessToken,
)