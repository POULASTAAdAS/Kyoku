package com.poulastaa.auth.data.repository.intro

import com.poulastaa.auth.data.mapper.toDtoUser
import com.poulastaa.auth.domain.intro.IntroLocalDatasource
import com.poulastaa.auth.domain.intro.IntroRemoteDatasource
import com.poulastaa.auth.domain.intro.IntroRepository
import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import jakarta.inject.Inject

internal class OnlineFirstIntroRepository @Inject constructor(
    private val remote: IntroRemoteDatasource,
    private val local: IntroLocalDatasource,
) : IntroRepository {
    override suspend fun emailLogIn(
        email: Email,
        password: Password,
    ): Result<DtoAuthResponseStatus, DataError.Network> {
        val response = remote.emailLogIn(email, password)
        if (response is Result.Success) {
            when (response.data.status) {
                DtoAuthResponseStatus.USER_FOUND,
                DtoAuthResponseStatus.USER_FOUND_NO_PLAYLIST,
                DtoAuthResponseStatus.USER_FOUND_NO_ARTIST,
                DtoAuthResponseStatus.USER_FOUND_NO_GENRE,
                DtoAuthResponseStatus.USER_FOUND_NO_B_DATE,
                    -> local.saveUser(response.data.toDtoUser())

                else -> Unit
            }
        }

        return response.map { it.status }
    }
}