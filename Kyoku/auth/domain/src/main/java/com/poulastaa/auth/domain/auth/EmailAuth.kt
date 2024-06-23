package com.poulastaa.auth.domain.auth

data class EmailAuth(
    val status: UserAuthStatus = UserAuthStatus.USER_NOT_FOUND,
    val accessToken: String = "",
    val refreshToken: String = "",
    val user: ResponseUser = ResponseUser(),
    // todo
)