package com.poulastaa.auth.domain

import com.poulastaa.common.domain.model.DtoUser
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiResult

interface AuthRepository {
    suspend fun signIn(email: String, password: String): ApiResult<DtoUser, ApiError>
}