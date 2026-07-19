package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.DtoAuthResponse
import com.poulastaa.auth.domain.model.DtoEmailAuthResponse
import com.poulastaa.auth.domain.model.DtoForgotPasswordResponse
import com.poulastaa.common.domain.model.DtoTokens
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiResult

interface AuthRemoteDatasource {
    suspend fun signIn(email: String, password: String): ApiResult<DtoEmailAuthResponse, ApiError>
    suspend fun signUp(email: String, username: String, password: String): ApiResult<DtoEmailAuthResponse, ApiError>
    suspend fun checkVerificationStatus(email: String): ApiResult<DtoTokens, ApiError>
    suspend fun googleAuth(token: String, countryCode: String): ApiResult<DtoAuthResponse, ApiError>
    suspend fun sendForgotPasswordMail(email: String): ApiResult<DtoForgotPasswordResponse, ApiError>
}
