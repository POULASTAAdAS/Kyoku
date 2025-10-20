package com.poulastaa.auth.domain

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.utils.JWTToken
import com.poulastaa.core.domain.Result

interface OtpValidationRepository {
    suspend fun validateOtp(otp: String): Result<JWTToken, DataError.Network>
}