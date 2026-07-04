package com.poulastaa.common.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin

actual fun platformHttpClient(
    block: HttpClientConfig<*>.() -> Unit,
): HttpClient = HttpClient(Darwin, block)
