package com.poulastaa.kyoku.data.repository

import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthHeaderInterceptor @Inject constructor(
    private val ds: DataStoreOperation,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()

        val authType = runBlocking(Dispatchers.IO) {
            ds.readAuthType().first()
        }

        val tokenOrCookie = runBlocking(Dispatchers.IO) {
            ds.readTokenOrCookie().first()
        }

        req.newBuilder()
            .header(
                name = if (authType == AuthType.JWT_AUTH.name) "Authorization" else "Cookie",
                value = tokenOrCookie
            )
            .build()

        return chain.proceed(req)
    }
}