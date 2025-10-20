package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.SingUpRepository
import com.poulastaa.auth.domain.email_signup.EmailSingUpRemoteDatasource
import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.utils.Username
import com.poulastaa.core.domain.map
import javax.inject.Inject

internal class OnlineFirstEmailSignUpRepository @Inject constructor(
    private val remote: EmailSingUpRemoteDatasource,
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
                // todo save user to db
            }
        }

        return response.map { it.status }
    }
}