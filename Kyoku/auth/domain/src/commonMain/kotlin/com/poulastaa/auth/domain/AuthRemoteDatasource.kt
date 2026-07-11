package com.poulastaa.auth.domain

import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiResult

interface AuthRemoteDatasource {
    suspend fun signIn(email: String, password: String): ApiResult<Unit, ApiError>
}