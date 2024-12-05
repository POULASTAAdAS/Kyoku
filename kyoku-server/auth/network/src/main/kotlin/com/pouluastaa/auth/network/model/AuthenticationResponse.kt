package com.pouluastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    val status: AuthenticationResponseStatus = AuthenticationResponseStatus.SERVER_ERROR,
    val user: ResponseUser = ResponseUser(),
    val token: JwtTokenResponse = JwtTokenResponse(),
)