package com.poulastaa.kyoku.domain.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.poulastaa.kyoku.data.model.auth.email.reafresh_token.RefreshTokenReq
import com.poulastaa.kyoku.data.model.auth.email.reafresh_token.RefreshTokenResponse
import com.poulastaa.kyoku.data.model.auth.email.reafresh_token.RefreshTokenUpdateStatus
import com.poulastaa.kyoku.utils.Constants.AUTH_BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshToken {
    @POST("/api/auth/refreshToken")
    suspend fun refreshToken(
        @Body req: RefreshTokenReq
    ): RefreshTokenResponse
}

suspend fun makeRefreshTokenCall(ds: DataStoreOperation) =
    withContext(Dispatchers.IO) {
        val refreshToken = async {
            ds.readRefreshToken().first()
        }.await()

        val response = async {
            Retrofit.Builder()
                .baseUrl(AUTH_BASE_URL)
                .client(
                    OkHttpClient
                        .Builder()
                        .build()
                )
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(RefreshToken::class.java)
                .refreshToken(
                    req = RefreshTokenReq(
                        oldRefreshToken = refreshToken
                    )
                )
        }.await()

        if (response.status == RefreshTokenUpdateStatus.UPDATED) {
            async {
                ds.storeRefreshToken("Bearer ${response.refreshToken}")
            }.await()

            async {
                ds.storeCookieOrAccessToken("Bearer ${response.accessToken}")
            }.await()
        }

        return@withContext response.accessToken
    }
