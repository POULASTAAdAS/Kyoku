package com.poulastaa.auth.data.repository.singup

import com.poulastaa.auth.data.mapper.toDtoUser
import com.poulastaa.auth.domain.email_signup.SingUpRepository
import com.poulastaa.auth.domain.email_signup.EmailSingUpLocalDatasource
import com.poulastaa.auth.domain.email_signup.EmailSingUpRemoteDatasource
import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.utils.Username
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class OnlineFirstEmailSignUpRepository @Inject constructor(
    private val remote: EmailSingUpRemoteDatasource,
    private val local: EmailSingUpLocalDatasource,
) : SingUpRepository {
    override suspend fun emailSingUp(
        email: Email,
        username: Username,
        password: Password,
        countryCode: String,
    ): Result<DtoAuthResponseStatus, DataError.Network> {
        val response = remote.emailSingUp(email, username, password, countryCode)

        if (response is Result.Success) {
            if (response.data.status == DtoAuthResponseStatus.USER_CREATED) {
                local.saveUser(response.data.toDtoUser())
            }
        }

        return response.map { it.status }
    }

    override suspend fun checkEmailVerificationStatus(
        email: Email,
    ): Result<Boolean, DataError.Network> {
        val result = remote.checkEmailVerificationStatus(email)
        if (result is Result.Success) {
            if (result.data.access.isNotBlank() && result.data.refresh.isNotBlank()) {
                // save token
                withContext(Dispatchers.IO) {
                    listOf(
                        async { local.saveAccessToken(result.data.access) },
                        async { local.saveRefreshToken(result.data.refresh) }
                    ).awaitAll()
                }

                return result.map { true }
            }
        }

        return result.map { false }
    }
}