package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.DtoForgotPasswordResponse
import com.poulastaa.common.domain.model.DtoUser
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.AppResult

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AppResult<Boolean, ApiError>
    suspend fun signUp(email: String, username: String, password: String): AppResult<Boolean, ApiError>
    suspend fun checkVerificationStatus(email: String): AppResult<Unit, ApiError>
    suspend fun googleAuth(token: String, countryCode: String): AppResult<DtoUser, ApiError>
    suspend fun sendForgotPasswordMail(email: String): AppResult<DtoForgotPasswordResponse, ApiError>
}
