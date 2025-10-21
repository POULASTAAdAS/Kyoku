package com.poulastaa.auth.domain.intro

import com.poulastaa.auth.domain.model.DtoGoogleAuth
import com.poulastaa.auth.domain.model.DtoResponseUser
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.DtoJWTToken
import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.domain.model.DtoUserType
import com.poulastaa.core.domain.utils.JWTToken

interface IntroRemoteDatasource {
    suspend fun emailLogIn(
        email: Email,
        password: Password,
    ): Result<DtoResponseUser, DataError.Network>

    suspend fun checkEmailVerificationStatus(
        email: Email,
        type: DtoUserType,
    ): Result<DtoJWTToken, DataError.Network>

    suspend fun googleOneTap(
        token: JWTToken,
        countryCode: String,
    ): Result<DtoGoogleAuth, DataError.Network>
}