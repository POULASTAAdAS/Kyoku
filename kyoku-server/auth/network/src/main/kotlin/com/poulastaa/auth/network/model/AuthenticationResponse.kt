package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    val status: AuthStatusResponse = AuthStatusResponse.SERVER_ERROR,
    val user: ResponseUser = ResponseUser(),
)