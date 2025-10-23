package com.poulastaa.auth.network.repository

import com.poulastaa.auth.domain.email_signup.EmailSingUpRemoteDatasource
import com.poulastaa.auth.domain.model.DtoResponseUser
import com.poulastaa.auth.network.domain.mapper.toDtoResponseUser
import com.poulastaa.auth.network.domain.model.request.RequestEmailSingUp
import com.poulastaa.auth.network.domain.model.response.ResponseAuth
import com.poulastaa.auth.network.domain.model.response.ResponseUserType
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.DtoJWTToken
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.utils.Username
import com.poulastaa.core.network.domain.model.DtoReqParam
import com.poulastaa.core.network.domain.model.Endpoints
import com.poulastaa.core.network.domain.model.ResponseJWTToken
import com.poulastaa.core.network.domain.repository.ApiRepository
import com.poulastaa.core.network.toDtoJWTToken
import javax.inject.Inject

internal class OkHttpEmailSingUiDatasource @Inject constructor(
    private val repo: ApiRepository,
) : EmailSingUpRemoteDatasource {
    override suspend fun emailSingUp(
        email: Email,
        username: Username,
        password: Password,
        countryCode: String,
    ): Result<DtoResponseUser, DataError.Network> =
        repo.authReq<RequestEmailSingUp, ResponseAuth>(
            route = Endpoints.EmailSingUp,
            method = ApiRepository.Method.POST,
            type = ResponseAuth::class.java,
            body = RequestEmailSingUp(
                email = email,
                username = username,
                password = password,
                countryCode = countryCode
            )
        ).map { it.toDtoResponseUser() }

    override suspend fun checkEmailVerificationStatus(
        email: Email,
    ): Result<DtoJWTToken, DataError.Network> = repo.authReq<Unit, ResponseJWTToken>(
        route = Endpoints.CheckVerificationMailStatus,
        method = ApiRepository.Method.GET,
        type = ResponseJWTToken::class.java,
        params = listOf(
            DtoReqParam("email", email),
            DtoReqParam("type", ResponseUserType.EMAIL.name),
        )
    ).map { it.toDtoJWTToken() }
}