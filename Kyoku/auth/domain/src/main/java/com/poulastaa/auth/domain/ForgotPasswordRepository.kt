package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.DtoForgotPasswordSentStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Result

interface ForgotPasswordRepository {
    suspend fun askForForgotPasswordMail(email: Email): Result<DtoForgotPasswordSentStatus, DataError.Network>
}