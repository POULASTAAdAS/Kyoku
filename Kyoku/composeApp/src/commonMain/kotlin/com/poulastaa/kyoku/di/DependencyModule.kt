package com.poulastaa.kyoku.di

import com.poulastaa.auth.data.RemoteAuthRepository
import com.poulastaa.auth.data.RoomDatasource
import com.poulastaa.auth.domain.AuthLocalDatasource
import com.poulastaa.auth.domain.AuthRemoteDatasource
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.network.KtorAuthDatasource
import com.poulastaa.common.network.platformHttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dependencyModule: Module = module {
    single { platformHttpClient() }
    singleOf(::KtorAuthDatasource) bind AuthRemoteDatasource::class
    singleOf(::RoomDatasource) bind AuthLocalDatasource::class
    singleOf(::RemoteAuthRepository) bind AuthRepository::class
}
