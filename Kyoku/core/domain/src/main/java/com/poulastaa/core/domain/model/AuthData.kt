package com.poulastaa.core.domain.model

data class AuthData(
    val status: UserAuthStatus = UserAuthStatus.USER_NOT_FOUND,
    val user: ResponseUser = ResponseUser(),
    val logInData: LogInData = LogInData(),
)
