package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.DtoForgotPasswordResponse
import com.poulastaa.auth.domain.model.DtoOtpValidationResponse
import com.poulastaa.common.domain.model.DtoUser
import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.domain.result.EmptyResponse

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AppResult<Boolean, ApiError>
    suspend fun signUp(email: String, username: String, password: String): AppResult<Boolean, ApiError>
    suspend fun checkVerificationStatus(email: String): AppResult<Unit, ApiError>
    suspend fun googleAuth(token: String): AppResult<DtoUser, ApiError>
    suspend fun sendForgotPasswordMail(email: String): AppResult<DtoForgotPasswordResponse, ApiError>
    suspend fun validateForgotPasswordOtp(email: String, otp: String): AppResult<DtoOtpValidationResponse, ApiError>
    suspend fun resetPassword(password: String, token: String): EmptyResponse<ApiError>
}
