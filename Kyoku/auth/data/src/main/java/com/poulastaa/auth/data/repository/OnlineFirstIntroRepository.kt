package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.IntroRepository
import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Password
import com.poulastaa.core.domain.Result
import jakarta.inject.Inject

internal class OnlineFirstIntroRepository @Inject constructor() : IntroRepository {
    override suspend fun emailLogIn(
        email: Email,
        password: Password,
    ): Result<DtoAuthResponseStatus, DataError.Network> {
        return Result.Success(DtoAuthResponseStatus.PASSWORD_DOES_NOT_MATCH)
    }
}