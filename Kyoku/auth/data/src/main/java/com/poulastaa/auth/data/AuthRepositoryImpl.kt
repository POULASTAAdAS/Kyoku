package com.poulastaa.auth.data

import com.google.gson.Gson
import com.poulastaa.auth.data.mapper.toUser
import com.poulastaa.auth.data.mapper.toUserAuthStatus
import com.poulastaa.auth.data.model.req.EmailLogInReq
import com.poulastaa.auth.data.model.req.EmailSignUpReq
import com.poulastaa.auth.data.model.req.GoogleAuthReq
import com.poulastaa.auth.data.model.res.EmailAuthDto
import com.poulastaa.auth.data.model.res.EmailVerificationDto
import com.poulastaa.auth.data.model.res.ForgotPasswordSetStatusDao
import com.poulastaa.auth.data.model.res.GoogleAuthDto
import com.poulastaa.auth.domain.auth.AuthRepository
import com.poulastaa.auth.domain.auth.ForgotPasswordSetStatus
import com.poulastaa.auth.domain.auth.UserAuthStatus
import com.poulastaa.core.data.network.authGet
import com.poulastaa.core.data.network.authPost
import com.poulastaa.core.data.network.getCookie
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.net.CookieManager
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(
    private val gson: Gson,
    @Named("AuthHttpClient") private val client: OkHttpClient,
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

    override suspend fun emailSignupStatusCheck(email: String): Boolean {
        val response = client.authGet<EmailVerificationDto>(
            route = EndPoints.SignUpEmailVerificationCheck.route,
            params = listOf(
                Pair(
                    first = "email",
                    second = email
                )
            ),
            gson = gson
        )

        if (response is Result.Success && response.data.status) {
            withContext(Dispatchers.IO) {
                val accessToken =
                    async { ds.storeTokenOrCookie("Bearer ${response.data.accessToken}") }
                val refreshToken = async { ds.storeRefreshToken(response.data.refreshToken) }
                val signInScreen = async { ds.storeSignInState(ScreenEnum.GET_SPOTIFY_PLAYLIST) }

                accessToken.await()
                refreshToken.await()
                signInScreen.await()
            }

            return true
        }
        return false
    }

    override suspend fun emailLoginStatusCheck(email: String): Boolean {
        val response = client.authGet<EmailVerificationDto>(
            route = EndPoints.LogInEmailVerificationCheck.route,
            params = listOf(
                Pair(
                    first = "email",
                    second = email
                )
            ),
            gson = gson
        )

        if (response is Result.Success && response.data.status) {
            withContext(Dispatchers.IO) {
                val accessToken =
                    async { ds.storeTokenOrCookie("Bearer ${response.data.accessToken}") }
                val refreshToken = async { ds.storeRefreshToken(response.data.refreshToken) }

                accessToken.await()
                refreshToken.await()
            }

            return true
        }
        return false
    }

    override suspend fun sendForgotPasswordMail(email: String): Result<ForgotPasswordSetStatus, DataError.Network> {
        val response = client.authGet<ForgotPasswordSetStatusDao>(
            route = EndPoints.ForgotPassword.route,
            params = listOf(
                Pair(
                    first = "email",
                    second = email
                )
            ),
            gson = gson
        )

        return response.map {
            when (it) {
                ForgotPasswordSetStatusDao.SENT -> ForgotPasswordSetStatus.SENT
                ForgotPasswordSetStatusDao.NO_USER_FOUND -> ForgotPasswordSetStatus.NO_USER_FOUND
            }
        }
    }
}