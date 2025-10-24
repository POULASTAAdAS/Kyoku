package com.poulastaa.core.network.data

import coil3.intercept.Interceptor
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageResult
import com.poulastaa.core.domain.repository.PreferencesDatastoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class ImageAuthHeaderInterceptor @Inject constructor(
    scope: CoroutineScope,
    private val ds: PreferencesDatastoreRepository,
) : Interceptor {
    var token: String? = null

    init {
        scope.launch {
            ds.getAccessTokenFlow().collectLatest { token ->
                token?.let { this@ImageAuthHeaderInterceptor.token = it }
            }
        }
    }

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        token?.let {
            chain.request.newBuilder()
                .httpHeaders(
                    NetworkHeaders.Builder()
                        .add("Authorization", "Bearer $it")
                        .build()
                ).build()
        } ?: chain.request

        return chain.proceed()
    }
}