package com.poulastaa.auth.domain.intro

import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.DtoUserType
import com.poulastaa.core.domain.utils.JWTToken

interface IntroRepository {
    suspend fun emailLogIn(
        email: Email,
        password: Password,
    ): Result<DtoAuthResponseStatus, DataError.Network>

    suspend fun checkEmailVerificationStatus(email: Email): Result<Boolean, DataError.Network>
    suspend fun googleOneTap(
        token: JWTToken,
        countryCode: String,
    ): Result<DtoAuthResponseStatus, DataError.Network>
}