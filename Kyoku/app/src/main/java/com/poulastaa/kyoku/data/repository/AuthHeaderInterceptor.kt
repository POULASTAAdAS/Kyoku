package com.poulastaa.kyoku.data.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.req.RefreshTokenReq
import com.poulastaa.kyoku.data.model.api.auth.email.RefreshTokenResponse
import com.poulastaa.kyoku.data.model.api.auth.email.RefreshTokenUpdateStatus
import com.poulastaa.kyoku.data.remote.RefreshTokenApi
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.utils.Constants.AUTH_BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import javax.inject.Inject

class AuthHeaderInterceptor @Inject constructor(
    private val ds: DataStoreOperation,
) : Interceptor {
    private fun refreshToken(ds: DataStoreOperation): RefreshTokenResponse {
        val oldRefreshToken = runBlocking {
            ds.readRefreshToken().first()
        }

        val response = runBlocking {
            Retrofit.Builder()
                .baseUrl(AUTH_BASE_URL)
                .client(OkHttpClient())
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(RefreshTokenApi::class.java)
                .refreshToken(
                    req = RefreshTokenReq(
                        oldRefreshToken = oldRefreshToken
                    )
                )
        }

        return when (response.status) {
            RefreshTokenUpdateStatus.UPDATED -> {
                runBlocking {
                    ds.storeRefreshToken(
                        data = "Bearer ${response.refreshToken}"
                    )
                }

                runBlocking {
                    ds.storeCookieOrAccessToken(data = "Bearer ${response.accessToken}")
                }

                response
            }

            else -> response
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldReq = chain.request()

        val authType = runBlocking(Dispatchers.IO) {
            ds.readAuthType().first()
        }

        val tokenOrCookie = runBlocking(Dispatchers.IO) {
            ds.readTokenOrCookie().first()
        }

        val newReq = oldReq.newBuilder()
            .header(
                name = if (authType == AuthType.JWT_AUTH.name) "Authorization" else "Cookie",
                value = tokenOrCookie
            )
            .build()

        val response = chain.proceed(newReq)

        if (response.code == 403) {
            return if (authType == AuthType.JWT_AUTH.name) {
                val refreshTokenResponse = refreshToken(ds)

                val makeReqAgain = oldReq.newBuilder()
                    .header(
                        name = "Authorization",
                        value = "Bearer ${refreshTokenResponse.accessToken}"
                    ).build()

                response.close()
                chain.proceed(makeReqAgain)
            } else response
        }

        if (authType == AuthType.SESSION_AUTH.name) {
            val cookie = response.header("set-cookie") ?: return response

            runBlocking {
                ds.storeCookieOrAccessToken(
                    data = cookie.split(";")[0]
                )
            }
        }

        return response
    }
}