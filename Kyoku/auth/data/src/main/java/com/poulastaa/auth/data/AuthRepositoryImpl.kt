package com.poulastaa.auth.data

import com.google.gson.Gson
import com.poulastaa.auth.data.mapper.toUser
import com.poulastaa.auth.data.mapper.toUserAuthStatus
import com.poulastaa.auth.data.model.req.EmailLogInReq
import com.poulastaa.auth.data.model.req.EmailSignUpReq
import com.poulastaa.auth.data.model.req.GoogleAuthReq
import com.poulastaa.auth.data.model.res.EmailAuthDto
import com.poulastaa.auth.data.model.res.GoogleAuthDto
import com.poulastaa.auth.domain.auth.AuthRepository
import com.poulastaa.auth.domain.auth.UserAuthStatus
import com.poulastaa.core.data.networking.authPost
import com.poulastaa.core.data.networking.getCookie
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.net.CookieManager
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val gson: Gson,
    private val client: OkHttpClient,
    private val ds: DataStoreRepository,
    private val cookieManager: CookieManager,
) : AuthRepository {
    override suspend fun emailSingUp(
        email: String,
        password: String,
        username: String,
        countryCode: String,
    ): Result<UserAuthStatus, DataError.Network> {
        val response = client.authPost<EmailSignUpReq, EmailAuthDto>(
            route = EndPoints.Auth.route,
            body = EmailSignUpReq(
                email = email,
                password = password,
                userName = username,
                countryCode = countryCode
            ),
            gson = gson
        )

        if (response is Result.Success) {
            ds.storeLocalUser(response.data.user.toUser())
        }

        return response.map {
            it.status.toUserAuthStatus()
        }
    }


    override suspend fun emailLogIn(
        email: String,
        password: String,
    ): Result<UserAuthStatus, DataError.Network> {
        val response = client.authPost<EmailLogInReq, EmailAuthDto>(
            route = EndPoints.Auth.route,
            body = EmailLogInReq(
                email = email,
                password = password,
            ),
            gson = gson
        )

        if (response is Result.Success) {
            ds.storeLocalUser(response.data.user.toUser())
        }

        return response.map {
            it.status.toUserAuthStatus()
        }
    }

    override suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): Result<UserAuthStatus, DataError.Network> {
        val response = client.authPost<GoogleAuthReq, GoogleAuthDto>(
            route = EndPoints.Auth.route,
            body = GoogleAuthReq(
                token = token,
                countryCode = countryCode
            ),
            gson = gson
        )

        if (response is Result.Success) {
            withContext(Dispatchers.IO) {
                val cookie = async { ds.storeTokenOrCookie(cookieManager.getCookie()) }
                val user = async { ds.storeLocalUser(response.data.user.toUser()) }

                cookie.await()
                user.await()
            }
        }

        return response.map {
            it.status.toUserAuthStatus()
        }
    }
}