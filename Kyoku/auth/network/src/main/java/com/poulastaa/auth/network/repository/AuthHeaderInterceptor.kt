package com.poulastaa.auth.network.repository

import com.poulastaa.auth.domain.model.JwtTokenDto
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.repository.DatastoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthHeaderInterceptor @Inject constructor(
    private val ds: DatastoreRepository,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldReq = chain.request()

        val tokenOrCookie = runBlocking {
            ds.readTokenOrCookie().first()
        }

        val newReq = oldReq.newBuilder()
            .addHeader(
                name = if (tokenOrCookie.startsWith("Bearer")) "Authorization" else "Cookie",
                value = tokenOrCookie
            )
            .build()

        val response = chain.proceed(newReq)

        if (response.code == 403 &&
            tokenOrCookie.startsWith("Bearer")
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

            } else response
        }

        if (response.code == 403) response.header("set-cookie")?.let {
            runBlocking {
                ds.storeTokenOrCookie(it.split(";")[0])
            }
        }

        return response
    }

    private suspend fun generateRefreshToken(
        ds: DatastoreRepository,
    ) = withContext(Dispatchers.IO) {
        val oldRefreshToken = ds.readRefreshToken()
        val client = OkHttpClient()

        try {
            val response = suspendCoroutine {
                client.newCall(
                    Request.Builder()
                        .url(EndPoints.GenerateRefreshToken.route)
                        .addHeader(
                            name = "oldRefreshToken",
                            value = oldRefreshToken
                        ).get()
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
            }

            response.use {
                if (!it.isSuccessful) return@withContext null

                val body = it.body?.string() ?: return@withContext null
                val result = Json.decodeFromString<JwtTokenDto>(body)

                if (result.refreshToken.isNotEmpty() && result.accessToken.isNotEmpty()) {
                    coroutineScope {
                        launch { ds.storeTokenOrCookie("Bearer ${result.accessToken}") }
                        launch { ds.storeRefreshToken(result.refreshToken) }
                    }
                }

                result
            }
        } catch (e: Exception) {
            null
        }
    }
}