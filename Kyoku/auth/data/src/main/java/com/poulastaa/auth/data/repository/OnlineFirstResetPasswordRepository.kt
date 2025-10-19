package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.ResetPasswordRepository
import com.poulastaa.auth.domain.model.DtoResetPasswordState
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.JWTToken
import com.poulastaa.core.domain.Password
import com.poulastaa.core.domain.Result
import javax.inject.Inject

internal class OnlineFirstResetPasswordRepository @Inject constructor() : ResetPasswordRepository {
    override suspend fun updatePassword(
        password: Password,
        token: JWTToken,
    ): Result<DtoResetPasswordState, DataError.Network> {
        return Result.Success(DtoResetPasswordState.SUCCESS)
    }
}