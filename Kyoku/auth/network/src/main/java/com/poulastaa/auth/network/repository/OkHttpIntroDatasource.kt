package com.poulastaa.auth.network.repository

import android.util.Log
import com.poulastaa.auth.domain.intro.IntroRemoteDatasource
import com.poulastaa.auth.domain.model.DtoGoogleAuth
import com.poulastaa.auth.domain.model.DtoResponseUser
import com.poulastaa.auth.network.domain.mapper.toDtoResponseUser
import com.poulastaa.auth.network.domain.model.request.RequestGoogleAuth
import com.poulastaa.auth.network.domain.model.request.RequestEmailLogIn
import com.poulastaa.auth.network.domain.model.response.ResponseAuth
import com.poulastaa.auth.network.domain.model.response.ResponseGoogleAuth
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.DtoJWTToken
import com.poulastaa.core.domain.model.DtoUserType
import com.poulastaa.core.domain.utils.JWTToken
import com.poulastaa.core.network.domain.model.DtoReqParam
import com.poulastaa.core.network.domain.model.Endpoints
import com.poulastaa.core.network.domain.model.ResponseJWTToken
import com.poulastaa.core.network.domain.repository.ApiRepository
import com.poulastaa.core.network.toDtoJWTToken
import jakarta.inject.Inject

internal class OkHttpIntroDatasource @Inject constructor(
    private val repo: ApiRepository,
) : IntroRemoteDatasource {
    override suspend fun emailLogIn(
        email: Email,
        password: Password,
    ): Result<DtoResponseUser, DataError.Network> =
        repo.authReq<RequestEmailLogIn, ResponseAuth>(
            route = Endpoints.EmailSingIn,
            method = ApiRepository.Method.POST,
            type = ResponseAuth::class.java,
            body = RequestEmailLogIn(
                email = email,
                password = password
            )
        ).map { it.toDtoResponseUser() }

    override suspend fun checkEmailVerificationStatus(
        email: Email,
        type: DtoUserType,
    ): Result<DtoJWTToken, DataError.Network> = repo.authReq<Unit, ResponseJWTToken>(
        route = Endpoints.CheckVerificationMailStatus,
        method = ApiRepository.Method.GET,
        type = ResponseJWTToken::class.java,
        params = listOf(
            DtoReqParam("email", email),
            DtoReqParam("type", type.name),
        )
    ).map { it.toDtoJWTToken() }

    override suspend fun googleOneTap(
        token: JWTToken,
        countryCode: String,
    ): Result<DtoGoogleAuth, DataError.Network> =
        repo.authReq<RequestGoogleAuth, ResponseGoogleAuth>(
            route = Endpoints.GoogleAuth,
            method = ApiRepository.Method.POST,
            type = ResponseGoogleAuth::class.java,
            body = RequestGoogleAuth(
                token = token,
                code = countryCode
            )
        ).map {
            DtoGoogleAuth(
                user = it.user.toDtoResponseUser(),
                token = it.token.toDtoJWTToken()
            )
        }
}