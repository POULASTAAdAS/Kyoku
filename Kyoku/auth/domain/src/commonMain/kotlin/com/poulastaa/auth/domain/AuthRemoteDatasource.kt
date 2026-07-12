package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.DtoAuthResponse
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiResult

interface AuthRemoteDatasource {
    suspend fun signIn(email: String, password: String): ApiResult<DtoAuthResponse, ApiError>
}