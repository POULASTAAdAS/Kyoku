package com.poulastaa.auth.domain.email_signup

import com.poulastaa.auth.domain.model.DtoResponseUser
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.DtoJWTToken
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.utils.Username

interface EmailSingUpRemoteDatasource {
    suspend fun emailSingUp(
        email: Email,
        username: Username,
        password: Password,
        countryCode: String,
    ): Result<DtoResponseUser, DataError.Network>

    suspend fun checkEmailVerificationStatus(email: Email): Result<DtoJWTToken, DataError.Network>
}