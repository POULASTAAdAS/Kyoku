package com.poulastaa.auth.data

import com.poulastaa.auth.data.mapper.toUser
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.domain.auth.AuthRepository
import com.poulastaa.core.domain.auth.LocalAuthDatasource
import com.poulastaa.core.domain.auth.RemoteAuthDatasource
import com.poulastaa.core.domain.model.ForgotPasswordSetStatus
import com.poulastaa.core.domain.model.UserAuthStatus
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.net.CookieManager
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val cookieManager: CookieManager,
    private val ds: DataStoreRepository,
    private val remote: RemoteAuthDatasource,
    private val local: LocalAuthDatasource,
    private val application: CoroutineScope,
) : AuthRepository {
    override suspend fun emailSingUp(
        email: String,
        password: String,
        username: String,
        countryCode: String,
    ): Result<UserAuthStatus, DataError.Network> {
        val result = remote.emailSingUp(
            email = email,
            password = password,
            username = username,
            countryCode = countryCode
        )

        if (result is Result.Success) {
            val storeUserDef = application.async {
                ds.storeLocalUser(result.data.user.toUser())
            }
            storeUserDef.await()
        }

        return result.map { it.status }
    }

    override suspend fun emailLogIn(
        email: String,
        password: String,
    ): Result<UserAuthStatus, DataError.Network> {
        val result = remote.emailLogIn(
            email = email,
            password = password,
        )

        if (result is Result.Success) {
            val storeUserDef = application.async {
                ds.storeLocalUser(result.data.user.toUser())
            }
            val storeDataDef = application.async {
                if (result.data.status == UserAuthStatus.CONFLICT ||
                    result.data.status == UserAuthStatus.USER_FOUND_STORE_B_DATE ||
                    result.data.status == UserAuthStatus.USER_FOUND_SET_ARTIST ||
                    result.data.status == UserAuthStatus.USER_FOUND_HOME ||
                    result.data.status == UserAuthStatus.USER_FOUND_SET_GENRE
                ) local.storeData(data = result.data.logInData)
            }

            storeUserDef.await()
            storeDataDef.await()
        }

        return result.map { it.status }
    }

    override suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): Result<UserAuthStatus, DataError.Network> {
        val result = remote.googleAuth(
            token = token,
            countryCode = countryCode
        )

        if (result is Result.Success) {
            val storeUserDef = application.async {
                ds.storeLocalUser(result.data.user.toUser())
            }
            val storeDataDef = application.async {
                if (result.data.status == UserAuthStatus.CONFLICT ||
                    result.data.status == UserAuthStatus.USER_FOUND_STORE_B_DATE ||
                    result.data.status == UserAuthStatus.USER_FOUND_SET_ARTIST ||
                    result.data.status == UserAuthStatus.USER_FOUND_HOME ||
                    result.data.status == UserAuthStatus.USER_FOUND_SET_GENRE
                ) {
                    val cookie = try {
                        cookieManager.cookieStore.cookies[0].toString()
                    } catch (e: Exception) {
                        null
                    }

                    if (cookie != null) {
                        async { ds.storeTokenOrCookie(cookie) }.await()
                        local.storeData(data = result.data.logInData)
                    }
                }
            }

            storeUserDef.await()
            storeDataDef.await()
        }

        return result.map { it.status }
    }

    override suspend fun emailSignupStatusCheck(email: String): Boolean {
        val response = remote.emailSignupStatusCheck(email)

        return if (response is Result.Success && response.data.status) {
            val accessToken =
                application.async { ds.storeTokenOrCookie("Bearer ${response.data.accessToken}") }
            val refreshToken =
                application.async { ds.storeRefreshToken(response.data.refreshToken) }
            val signInScreen =
                application.async { ds.storeSignInState(ScreenEnum.GET_SPOTIFY_PLAYLIST) }

            accessToken.await()
            refreshToken.await()
            signInScreen.await()

            true
        } else false
    }


    override suspend fun emailLoginStatusCheck(email: String): Boolean {
        val response = remote.emailLoginStatusCheck(email)

        return if (response is Result.Success && response.data.status) {
            val accessToken =
                application.async { ds.storeTokenOrCookie("Bearer ${response.data.accessToken}") }
            val refreshToken =
                application.async { ds.storeRefreshToken(response.data.refreshToken) }
            val signInScreen =
                application.async { ds.storeSignInState(ScreenEnum.GET_SPOTIFY_PLAYLIST) }

            accessToken.await()
            refreshToken.await()
            signInScreen.await()

            true
        } else false
    }

    override suspend fun sendForgotPasswordMail(email: String): Result<ForgotPasswordSetStatus, DataError.Network> =
        remote.sendForgotPasswordMail(email)
}