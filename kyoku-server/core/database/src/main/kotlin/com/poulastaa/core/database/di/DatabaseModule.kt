package com.poulastaa.core.database.di

import com.poulastaa.core.database.RedisDbManager
import com.poulastaa.core.database.repository.ExposedLocalAuthDatasource
import com.poulastaa.core.database.repository.RedisLocalCacheDataSource
import com.poulastaa.core.domain.repository.LocalAuthDatasource
import com.poulastaa.core.domain.repository.LocalCacheDatasource
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
    single<LocalCacheDatasource> {
        RedisLocalCacheDataSource(
            redisPool = get(),
            gson = get()
        )
    }

    single<LocalAuthDatasource> {
        ExposedLocalAuthDatasource(cache = get())
    }
}