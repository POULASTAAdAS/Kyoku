package com.poulastaa.auth.network.repository

import com.poulastaa.auth.domain.model.DtoResetPasswordState
import com.poulastaa.auth.domain.update_password.ResetPasswordRemoteDatasource
import com.poulastaa.auth.network.domain.mapper.toDtoUpdatePasswordStatus
import com.poulastaa.auth.network.domain.model.request.RequestUpdatePassword
import com.poulastaa.auth.network.domain.model.response.UpdatePasswordResponse
import com.poulastaa.auth.network.domain.model.response.UpdatePasswordStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.utils.JWTToken
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.network.domain.model.Endpoints
import com.poulastaa.core.network.domain.repository.ApiRepository
import javax.inject.Inject


internal class OkHttpResetPasswordDatasource @Inject constructor(
    private val api: ApiRepository,
) : ResetPasswordRemoteDatasource {
    override suspend fun updatePassword(
        password: Password,
        token: JWTToken,
    ): Result<DtoResetPasswordState, DataError.Network> =
        api.authReq<RequestUpdatePassword, UpdatePasswordResponse>(
            route = Endpoints.UpdatePassword,
            method = ApiRepository.Method.POST,
            type = UpdatePasswordResponse::class.java,
            body = RequestUpdatePassword(
                password = password,
                token = token
            )
        ).map { it.toDtoUpdatePasswordStatus() }
}