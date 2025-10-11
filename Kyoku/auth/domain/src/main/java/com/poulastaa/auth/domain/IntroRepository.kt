package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.response.DtoResponseStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Password
import com.poulastaa.core.domain.Result

interface IntroRepository {
    suspend fun emailLogIn(
        email: Email,
        password: Password,
    ): Result<DtoResponseStatus, DataError.Network>
}