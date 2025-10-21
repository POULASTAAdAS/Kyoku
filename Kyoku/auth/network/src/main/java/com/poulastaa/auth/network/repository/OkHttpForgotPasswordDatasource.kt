package com.poulastaa.auth.network.repository

import android.util.Log
import com.poulastaa.auth.domain.forgot_password.ForgotPasswordRemoteDatasource
import com.poulastaa.auth.domain.model.DtoForgotPasswordSentStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.network.domain.model.DtoReqParam
import com.poulastaa.core.network.domain.model.Endpoints
import com.poulastaa.core.network.domain.repository.ApiRepository
import javax.inject.Inject

internal class OkHttpForgotPasswordDatasource @Inject constructor(
    private val repo: ApiRepository,
) : ForgotPasswordRemoteDatasource {
    override suspend fun askForValidationCode(
        email: Email,
    ): Result<DtoForgotPasswordSentStatus, DataError.Network> = try {
        repo.authReq<Unit, String>(
            route = Endpoints.RequestForVerificationCode,
            method = ApiRepository.Method.GET,
            type = String::class.java,
            params = listOf(DtoReqParam("email", email))
        ).map {
            DtoForgotPasswordSentStatus.valueOf(it)
        }
    } catch (e: Exception) {
        Log.e("askForValidationCode", e.message.toString())
        Result.Error(DataError.Network.SERVER_ERROR)
    }

}