package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.model.DtoResetPasswordState
import com.poulastaa.auth.domain.update_password.ResetPasswordRemoteDatasource
import com.poulastaa.auth.domain.update_password.ResetPasswordRepository
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.utils.JWTToken
import com.poulastaa.core.domain.utils.Password
import javax.inject.Inject

internal class OnlineFirstResetPasswordRepository @Inject constructor(
    private val remote: ResetPasswordRemoteDatasource,
) : ResetPasswordRepository {
    override suspend fun updatePassword(
        password: Password,
        token: JWTToken,
    ): Result<DtoResetPasswordState, DataError.Network> = remote.updatePassword(password, token)
}