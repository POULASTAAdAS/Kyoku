package com.poulastaa.auth.domain.forgot_password

import com.poulastaa.auth.domain.model.DtoForgotPasswordSentStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.utils.Email

interface ForgotPasswordRemoteDatasource {
    suspend fun askForValidationCode(email: Email): Result<DtoForgotPasswordSentStatus, DataError.Network>
}