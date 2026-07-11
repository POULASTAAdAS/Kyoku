package com.poulastaa.common.network.di

import com.poulastaa.common.network.PlatformHttpClient
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

val commonNetworkModule: Module = module {
    single { PlatformHttpClient() }
    single<HttpClient> { get<PlatformHttpClient>().client }
    single<Json> { PlatformHttpClient.json }
}

