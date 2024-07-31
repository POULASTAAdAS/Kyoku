package com.poulastaa.data.model.auth.response

import com.poulastaa.data.model.User
import com.poulastaa.data.model.auth.response.login.ApiLogInDto
import com.poulastaa.data.model.auth.response.login.LogInDto
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthRes(
    val status: UserAuthStatus = UserAuthStatus.USER_NOT_FOUND,
    val user: User = User(),
    val logInData: LogInDto = LogInDto(),
)
