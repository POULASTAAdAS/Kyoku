package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.IntroRepository
import com.poulastaa.auth.domain.model.response.DtoResponseStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Password
import com.poulastaa.core.domain.Result
import jakarta.inject.Inject

class OnlineFirstIntroRepository @Inject constructor() : IntroRepository {
    override suspend fun emailLogIn(
        email: Email,
        password: Password,
    ): Result<DtoResponseStatus, DataError.Network> {
        return Result.Success(DtoResponseStatus.PASSWORD_DOES_NOT_MATCH)
    }
}