package com.poulastaa.auth.network.repository

import com.poulastaa.auth.domain.intro.IntroRemoteDatasource
import com.poulastaa.auth.domain.model.DtoResponseUser
import com.poulastaa.auth.network.domain.mapper.toDtoUser
import com.poulastaa.auth.network.domain.model.request.RequestEmailLogIn
import com.poulastaa.auth.network.domain.model.response.ResponseEmailLogIn
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Password
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.network.domain.model.Endpoints
import com.poulastaa.core.network.domain.repository.ApiRepository
import jakarta.inject.Inject

internal class OkHttpIntroDatasource @Inject constructor(
    private val repo: ApiRepository,
) : IntroRemoteDatasource {
    override suspend fun emailLogIn(
        email: Email,
        password: Password,
    ): Result<DtoResponseUser, DataError.Network> =
        repo.authReq<RequestEmailLogIn, ResponseEmailLogIn>(
            route = Endpoints.EmailSingIn,
            method = ApiRepository.Method.POST,
            type = ResponseEmailLogIn::class.java,
            body = RequestEmailLogIn(
                email = email,
                password = password
            )
        ).map { it.toDtoUser() }
}