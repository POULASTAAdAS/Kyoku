package com.poulastaa.auth.domain.email_signup

import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.utils.Username

interface SingUpRepository {
    suspend fun emailSingUp(
        email: Email,
        username: Username,
        password: Password,
        countryCode: String,
    ): Result<DtoAuthResponseStatus, DataError.Network>

    suspend fun checkEmailVerificationStatus(email: Email): Result<Boolean, DataError.Network>
}