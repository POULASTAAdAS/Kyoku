package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.OtpValidationRepository
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.JWTToken
import com.poulastaa.core.domain.Result
import javax.inject.Inject

internal class OnlineFirstOtpValidationRepository @Inject constructor() : OtpValidationRepository {
    override suspend fun validateOtp(otp: String): Result<JWTToken, DataError.Network> {
        return Result.Success("tempToken")
    }
}