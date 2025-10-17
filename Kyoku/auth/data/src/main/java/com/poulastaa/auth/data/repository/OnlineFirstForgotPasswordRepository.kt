package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.ForgotPasswordRepository
import com.poulastaa.auth.domain.model.DtoForgotPasswordSentStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Result
import javax.inject.Inject

internal class OnlineFirstForgotPasswordRepository @Inject constructor() :
    ForgotPasswordRepository {
    override suspend fun askForForgotPasswordMail(email: Email): Result<DtoForgotPasswordSentStatus, DataError.Network> {
        return Result.Success(DtoForgotPasswordSentStatus.SENT)
    }
}