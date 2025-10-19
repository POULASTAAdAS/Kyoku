package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.intro.IntroRemoteDatasource
import com.poulastaa.auth.domain.intro.IntroRepository
import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Password
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import jakarta.inject.Inject

internal class OnlineFirstIntroRepository @Inject constructor(
    private val remote: IntroRemoteDatasource,
) : IntroRepository {
    override suspend fun emailLogIn(
        email: Email,
        password: Password,
    ): Result<DtoAuthResponseStatus, DataError.Network> {
        val response = remote.emailLogIn(email, password)
        if (response is Result.Success) {
            when (response.data.status) {
                DtoAuthResponseStatus.USER_CREATED,
                DtoAuthResponseStatus.USER_FOUND,
                DtoAuthResponseStatus.USER_FOUND_NO_PLAYLIST,
                DtoAuthResponseStatus.USER_FOUND_NO_ARTIST,
                DtoAuthResponseStatus.USER_FOUND_NO_GENRE,
                DtoAuthResponseStatus.USER_FOUND_NO_B_DATE,
                    -> {
                    //todo save user
                }

                else -> Unit
            }
        }

        return response.map { it.status }
    }
}