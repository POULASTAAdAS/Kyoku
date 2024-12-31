package com.poulastaa.core.database.di

import com.poulastaa.core.database.RedisDbManager
import com.poulastaa.core.database.repository.*
import com.poulastaa.core.database.repository.auth.ExposedLocalAuthDatasource
import com.poulastaa.core.database.repository.auth.RedisLocalAuthDataSource
import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.LocalCoreDatasource
import com.poulastaa.core.domain.repository.auth.LocalAuthCacheDatasource
import com.poulastaa.core.domain.repository.auth.LocalAuthDatasource
import com.poulastaa.core.domain.repository.auth.LocalSessionCacheDatasource
import org.koin.dsl.module
import redis.clients.jedis.JedisPool

fun provideJedisPoolService(
    redisHost: String,
    redisPort: Int,
    redisPassword: String,
) = module {
    single<JedisPool> {
        RedisDbManager.initializeRedisDatabases(
            redisHost = redisHost,
            redisPort = redisPort,
            redisPassword = redisPassword
        )
    }
}

fun provideCoreDatabaseService() = module {
    single<LocalCoreCacheDatasource> {
        RedisLocalCoreDatasource(
            redisPool = get(),
            gson = get()
        )
    }

    single<LocalAuthCacheDatasource> {
        RedisLocalAuthDataSource(
            redisPool = get(),
            gson = get(),
            coreCache = get()
        )
    }

    single<LocalCoreDatasource> {
        ExposedLocalCoreDatasource(cache = get())
    }

    single<LocalAuthDatasource> {
        ExposedLocalAuthDatasource(
            coreDB = get(),
            cache = get()
        )
    }

    single<LocalSessionCacheDatasource> {
        RedisLocalSessionCacheDatasource(redisPool = get())
    }
}