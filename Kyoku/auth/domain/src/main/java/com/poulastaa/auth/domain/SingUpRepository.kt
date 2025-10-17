package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Password
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.Username

interface SingUpRepository {
    suspend fun emailSingUp(
        email: Email,
        username: Username,
        password: Password,
    ): Result<DtoAuthResponseStatus, DataError.Network>
}