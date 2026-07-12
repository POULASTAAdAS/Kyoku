package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.DtoAuthResponse
import com.poulastaa.auth.domain.model.DtoForgotPasswordResponse
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiResult

interface AuthRemoteDatasource {
    suspend fun signIn(email: String, password: String): ApiResult<DtoAuthResponse, ApiError>
    suspend fun googleAuth(token: String, countryCode: String): ApiResult<DtoAuthResponse, ApiError>
    suspend fun sendForgotPasswordMail(email: String): ApiResult<DtoForgotPasswordResponse, ApiError>
}
