package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.model.DtoValidateOTPPayload
import com.poulastaa.auth.domain.otp.OtpValidationRemoteDataSource
import com.poulastaa.auth.domain.otp.OtpValidationRepository
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.utils.Email
import javax.inject.Inject

internal class OnlineFirstOtpValidationRepository @Inject constructor(
    private val remote: OtpValidationRemoteDataSource,
) : OtpValidationRepository {
    override suspend fun validateOtp(
        otp: String,
        email: Email,
    ): Result<DtoValidateOTPPayload, DataError.Network> = remote.validateOtp(otp, email)
}