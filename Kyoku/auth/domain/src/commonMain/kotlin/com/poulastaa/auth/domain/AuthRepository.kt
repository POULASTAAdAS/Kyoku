package com.poulastaa.auth.domain

import com.poulastaa.common.network.EmptyResponse
import com.poulastaa.common.network.Error

interface AuthRepository {
    suspend fun signIn(email: String, password: String): EmptyResponse<Error>
}