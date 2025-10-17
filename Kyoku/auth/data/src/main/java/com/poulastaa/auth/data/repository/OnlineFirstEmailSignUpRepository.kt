package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.SingUpRepository
import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Password
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.Username
import javax.inject.Inject

internal class OnlineFirstEmailSignUpRepository @Inject constructor() : SingUpRepository {
    override suspend fun emailSingUp(
        email: Email,
        username: Username,
        password: Password,
    ): Result<DtoAuthResponseStatus, DataError.Network> {
        return Result.Success(DtoAuthResponseStatus.EMAIL_ALREADY_IN_USE)
    }
}