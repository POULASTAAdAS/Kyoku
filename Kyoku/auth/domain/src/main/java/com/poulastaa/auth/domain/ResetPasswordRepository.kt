package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.DtoResetPasswordState
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.JWTToken
import com.poulastaa.core.domain.Password
import com.poulastaa.core.domain.Result

interface ResetPasswordRepository {
    suspend fun updatePassword(
        password: Password,
        token: JWTToken,
    ): Result<DtoResetPasswordState, DataError.Network>
}