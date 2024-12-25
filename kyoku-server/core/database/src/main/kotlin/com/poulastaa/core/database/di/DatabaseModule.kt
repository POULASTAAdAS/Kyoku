package com.poulastaa.core.database.di

import com.poulastaa.core.database.RedisDbManager
import com.poulastaa.core.database.repository.ExposedLocalAuthDatasource
import com.poulastaa.core.database.repository.RedisLocalAuthCacheDataSource
import com.poulastaa.core.database.repository.RedisLocalSessionCacheDatasource
import com.poulastaa.core.domain.repository.LocalAuthCacheDatasource
import com.poulastaa.core.domain.repository.LocalAuthDatasource
import com.poulastaa.core.domain.repository.LocalSessionCacheDatasource
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

fun provideUserDatabaseService() = module {
    single<LocalAuthCacheDatasource> {
        RedisLocalAuthCacheDataSource(
            redisPool = get(),
            gson = get()
        )
    }

    single<LocalAuthDatasource> {
        ExposedLocalAuthDatasource(cache = get())
    }

    single<LocalSessionCacheDatasource> {
        RedisLocalSessionCacheDatasource(redisPool = get())
    }
}