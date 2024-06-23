package com.poulastaa.auth.domain.auth

data class GoogleAuth(
    val status: UserAuthStatus = UserAuthStatus.USER_NOT_FOUND,
    val user: ResponseUser = ResponseUser(),
)