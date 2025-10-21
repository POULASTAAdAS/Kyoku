package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.forgot_password.ForgotPasswordRemoteDatasource
import com.poulastaa.auth.domain.forgot_password.ForgotPasswordRepository
import com.poulastaa.auth.domain.model.DtoForgotPasswordSentStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.Result
import javax.inject.Inject

internal class OnlineFirstForgotPasswordRepository @Inject constructor(
    private val remote: ForgotPasswordRemoteDatasource,
) : ForgotPasswordRepository {
    override suspend fun askForForgotPasswordMail(
        email: Email,
    ): Result<DtoForgotPasswordSentStatus, DataError.Network> = remote.askForValidationCode(email)
}