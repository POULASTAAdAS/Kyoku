package com.poulastaa.auth.network.repository

import com.poulastaa.auth.domain.email_signup.EmailSingUpRemoteDatasource
import com.poulastaa.auth.domain.model.DtoResponseUser
import com.poulastaa.auth.network.domain.mapper.toDtoResponseUser
import com.poulastaa.auth.network.domain.model.request.RequestEmailSingUp
import com.poulastaa.auth.network.domain.model.response.ResponseEmailAuth
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.utils.Username
import com.poulastaa.core.domain.map
import com.poulastaa.core.network.domain.model.Endpoints
import com.poulastaa.core.network.domain.repository.ApiRepository
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
        repo.authReq<RequestEmailSingUp, ResponseEmailAuth>(
            route = Endpoints.EmailSingUp,
            method = ApiRepository.Method.POST,
            type = ResponseEmailAuth::class.java,
            body = RequestEmailSingUp(
                email = email,
                username = username,
                password = password,
                countryCode = countryCode
            )
        ).map { it.toDtoResponseUser() }
}