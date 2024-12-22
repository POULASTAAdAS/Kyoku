package com.poulastaa.auth.data

import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.RemoteAuthDataSource
import com.poulastaa.auth.domain.model.AuthStatus
import com.poulastaa.auth.domain.model.EmailVerificationStatus
import com.poulastaa.auth.domain.model.ForgotPasswordStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.DatastoreRepository
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
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
            ds.storeLocalUser(result.data.user)

            val cookie = cookieManager.cookieStore.cookies.firstOrNull()?.toString()
                ?: return Result.Error(DataError.Network.SERVER_ERROR)

            ds.storeTokenOrCookie(cookie)
        }

        return result.map { it.status }
    }

    override suspend fun checkEmailVerificationState(email: String): Result<EmailVerificationStatus, DataError.Network> {
        val result = remote.checkEmailVerificationState(email)

        if (result is Result.Success && result.data == EmailVerificationStatus.VERIFIED) {
            val token = remote.getJWTToken(email)

            if (token is Result.Success) {
                ds.storeTokenOrCookie("Bearer ${token.data.accessToken}")
                ds.storeRefreshToken(token.data.refreshToken)
            }
        }

        return result
    }

    override suspend fun sendForgotPasswordMail(email: String): Result<ForgotPasswordStatus, DataError.Network> =
        remote.sendForgotPasswordMail(email)
}