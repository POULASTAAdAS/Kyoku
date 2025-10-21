package com.poulastaa.auth.domain.update_password

import com.poulastaa.auth.domain.model.DtoResetPasswordState
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.utils.JWTToken
import com.poulastaa.core.domain.utils.Password

interface ResetPasswordRepository {
    suspend fun updatePassword(
        password: Password,
        token: JWTToken,
    ): Result<DtoResetPasswordState, DataError.Network>
}