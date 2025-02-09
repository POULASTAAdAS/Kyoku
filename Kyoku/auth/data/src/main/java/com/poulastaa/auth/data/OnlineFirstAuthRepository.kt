package com.poulastaa.auth.data

import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.RemoteAuthDataSource
import com.poulastaa.auth.domain.model.AuthStatus
import com.poulastaa.auth.domain.model.ForgotPasswordStatus
import com.poulastaa.auth.domain.model.PasskeyPayload
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.domain.repository.DatastoreRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.net.CookieManager
import javax.inject.Inject

class OnlineFirstAuthRepository @Inject constructor(
    private val ds: DatastoreRepository,
    private val remote: RemoteAuthDataSource,
    private val cookieManager: CookieManager,
) : AuthRepository {
    override suspend fun emailSignUp(
        email: String,
        password: String,
        username: String,
        countryCode: String,
    ): Result<AuthStatus, DataError.Network> {
        val result = remote.emailSingUp(
            email = email,
            password = password,
            username = username,
            countryCode = countryCode
        )

        if (result is Result.Success && result.data.user.email.isNotEmpty())
            ds.storeLocalUser(result.data.user)

        return result.map { it.status }
    }

    override suspend fun emailLogIn(
        email: String,
        password: String,
    ): Result<AuthStatus, DataError.Network> {
        val result = remote.emailLogIn(
            email = email,
            password = password
        )

        if (result is Result.Success && result.data.user.email.isNotEmpty())
            ds.storeLocalUser(result.data.user)

        return result.map { it.status }
    }

    override suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): Result<AuthStatus, DataError.Network> {
        val result = remote.googleAuth(
            token = token,
            countryCode = countryCode
        )

        if ((result is Result.Success && result.data.user.email.isNotEmpty()) && (
                    result.data.status == AuthStatus.CREATED ||
                            result.data.status == AuthStatus.USER_FOUND ||
                            result.data.status == AuthStatus.USER_FOUND_STORE_B_DATE ||
                            result.data.status == AuthStatus.USER_FOUND_SET_GENRE ||
                            result.data.status == AuthStatus.USER_FOUND_SET_ARTIST ||
                            result.data.status == AuthStatus.USER_FOUND_HOME
                    )
        ) {
            val cookie = cookieManager.cookieStore.cookies.firstOrNull()?.toString()
                ?: return Result.Error(DataError.Network.SERVER_ERROR)
            val screen = when (result.data.status) {
                AuthStatus.CREATED -> SavedScreen.IMPORT_SPOTIFY_PLAYLIST
                AuthStatus.USER_FOUND, AuthStatus.USER_FOUND_HOME -> SavedScreen.HOME
                AuthStatus.USER_FOUND_STORE_B_DATE -> SavedScreen.SET_B_DATE
                AuthStatus.USER_FOUND_SET_GENRE -> SavedScreen.PIC_GENRE
                AuthStatus.USER_FOUND_SET_ARTIST -> SavedScreen.PIC_ARTIST
                else -> return result.map { it.status }
            }

            coroutineScope {
                listOf(
                    async { ds.storeTokenOrCookie(cookie) },
                    async { ds.storeLocalUser(result.data.user) },
                    async { ds.storeSignInState(screen) }
                )
            }.awaitAll()
        }

        return result.map { it.status }
    }

    override suspend fun checkEmailVerificationState(
        email: String,
        resultState: AuthStatus,
    ): Result<Boolean, DataError.Network> {
        val result = remote.checkEmailVerificationState(email)

        if (result is Result.Success && result.data.status) {
            ds.storeTokenOrCookie("Bearer ${result.data.accessToken}")
            ds.storeRefreshToken(result.data.refreshToken)

            when (resultState) {
                AuthStatus.CREATED,
                AuthStatus.USER_FOUND,
                    -> ds.storeSignInState(SavedScreen.IMPORT_SPOTIFY_PLAYLIST)

                AuthStatus.USER_FOUND_STORE_B_DATE -> ds.storeSignInState(SavedScreen.SET_B_DATE)
                AuthStatus.USER_FOUND_SET_GENRE -> ds.storeSignInState(SavedScreen.PIC_GENRE)
                AuthStatus.USER_FOUND_SET_ARTIST -> ds.storeSignInState(SavedScreen.PIC_ARTIST)
                AuthStatus.USER_FOUND_HOME -> ds.storeSignInState(SavedScreen.HOME)

                else -> Unit
            }

            return result.map { it.status }
        }

        return result.map { it.status }
    }

    override suspend fun sendForgotPasswordMail(email: String): Result<ForgotPasswordStatus, DataError.Network> =
        remote.sendForgotPasswordMail(email)

    override suspend fun getPasskeyRequest(email: String): PasskeyPayload =
        TODO("can only be implemented for https request")
}