package com.poulastaa.auth.network.repository

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.poulastaa.auth.network.BuildConfig
import com.poulastaa.auth.network.model.JwtTokenResponse
import com.poulastaa.auth.network.model.RefreshTokenRequest
import com.poulastaa.auth.network.toJWTTokenDto
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.network.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthHeaderInterceptor @Inject constructor(
    private val ds: DatastoreRepository,
    private val gson: Gson,
    @ApplicationContext private val context: Context,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldReq = chain.request()
        val tokenOrCookie = runBlocking { ds.readTokenOrCookie().first() }

        val newReq = oldReq.newBuilder()
            .addHeader(
                name = if (tokenOrCookie.startsWith("Bearer")) "Authorization" else "Cookie",
                value = tokenOrCookie
            )
            .build()

        val response = chain.proceed(newReq)

        if (response.code == 403 || response.code == 401 &&
            tokenOrCookie.startsWith("Bearer", ignoreCase = true)
        ) {
            val result = runBlocking { generateRefreshToken(ds) }

            return if (result != null) {
                val resumeReq = oldReq.newBuilder()
                    .header(
                        name = "Authorization",
                        value = "Bearer ${result.accessToken}"
                    ).build()

                response.close()
                chain.proceed(resumeReq)
            } else {
                runBlocking {
                    ds.logOut()

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.session_expired),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                response
            }
        } else response.header("set-cookie")?.let {
            runBlocking {
                ds.storeTokenOrCookie(it.split(";")[0])
            }
        }

        return response
    }

    private suspend fun generateRefreshToken(
        ds: DatastoreRepository,
    ) = withContext(Dispatchers.IO) {
        val oldRefreshTokenDef = async { ds.readRefreshToken() }
        val emailDef = async { ds.readLocalUser().email }

        val oldRefreshToken = oldRefreshTokenDef.await()
        val email = emailDef.await()

        val client = OkHttpClient()

        try {
            val url = "${BuildConfig.BASE_URL}${EndPoints.RefreshToken.route}".toHttpUrlOrNull()
                ?.newBuilder()?.build() ?: return@withContext null

            val response = suspendCoroutine {
                try {
                    client.newCall(
                        Request.Builder()
                            .url(url)
                            .post(
                                body = gson.toJson(
                                    RefreshTokenRequest(
                                        email = email,
                                        token = oldRefreshToken
                                    )
                                ).toRequestBody("application/json; charset=utf-8".toMediaType())
                            )
                            .build()
                    ).enqueue(
                        object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                it.resumeWithException(e)
                            }

                            override fun onResponse(call: Call, response: Response) {
                                it.resume(response)
                            }
                        }
                    )
                } catch (e: Exception) {
                    it.resumeWithException(e)
                }
            }

            response.use {
                if (!it.isSuccessful) return@withContext null

                val body = it.body?.string() ?: return@withContext null
                val result = Json.decodeFromString<JwtTokenResponse>(body).toJWTTokenDto()

                if (result.refreshToken.isNotEmpty() && result.accessToken.isNotEmpty()) listOf(
                    async { ds.storeTokenOrCookie("Bearer ${result.accessToken}") },
                    async { ds.storeRefreshToken(result.refreshToken) }
                ).awaitAll()
                it.close()

                result
            }
        } catch (e: Exception) {
            null
        }
    }
}