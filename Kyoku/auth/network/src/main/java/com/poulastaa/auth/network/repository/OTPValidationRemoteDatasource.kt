package com.poulastaa.auth.network.repository

import com.poulastaa.auth.domain.model.DtoValidateOTPPayload
import com.poulastaa.auth.domain.otp.OtpValidationRemoteDataSource
import com.poulastaa.auth.network.domain.mapper.toDtoValidationOTPPayload
import com.poulastaa.auth.network.domain.model.response.ResponseValidateOTP
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.network.domain.model.DtoReqParam
import com.poulastaa.core.network.domain.model.Endpoints
import com.poulastaa.core.network.domain.repository.ApiRepository
import javax.inject.Inject

internal class OTPValidationRemoteDatasource @Inject constructor(
    private val api: ApiRepository,
) : OtpValidationRemoteDataSource {
    override suspend fun validateOtp(
        otp: String,
        email: Email,
    ): Result<DtoValidateOTPPayload, DataError.Network> = api.authReq<Unit, ResponseValidateOTP>(
        route = Endpoints.ValidateForgotPasswordCode,
        method = ApiRepository.Method.GET,
        type = ResponseValidateOTP::class.java,
        params = listOf(
            DtoReqParam("email", email),
            DtoReqParam("code", otp),
        )
    ).map { it.toDtoValidationOTPPayload() }
}