package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.DtoForgotPasswordResponse
import com.poulastaa.common.domain.model.DtoUser
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiResult

interface AuthRepository {
    suspend fun signIn(email: String, password: String): ApiResult<DtoUser, ApiError>
    suspend fun signUp(email: String, username: String, password: String): ApiResult<DtoUser, ApiError>
    suspend fun googleAuth(token: String, countryCode: String): ApiResult<DtoUser, ApiError>
    suspend fun sendForgotPasswordMail(email: String): ApiResult<DtoForgotPasswordResponse, ApiError>
}
