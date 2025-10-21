package com.poulastaa.auth.domain.otp

import com.poulastaa.auth.domain.model.DtoValidateOTPPayload
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.utils.Email

interface OtpValidationRepository {
    suspend fun validateOtp(
        otp: String,
        email: Email,
    ): Result<DtoValidateOTPPayload, DataError.Network>
}