package com.poulastaa.auth.domain.intro

import com.poulastaa.auth.domain.model.DtoResponseUser
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.Result

interface IntroRemoteDatasource {
    suspend fun emailLogIn(
        email: Email,
        password: Password
    ): Result<DtoResponseUser, DataError.Network>
}