package com.poulastaa.auth.domain.intro

import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.Result

interface IntroRepository {
    suspend fun emailLogIn(
        email: Email,
        password: Password,
    ): Result<DtoAuthResponseStatus, DataError.Network>
}