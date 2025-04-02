package com.poulastaa.core.database.di

import com.poulastaa.core.database.RedisDbManager
import com.poulastaa.core.database.repository.ExposedLocalCoreDatasource
import com.poulastaa.core.database.repository.RedisLocalCoreDatasource
import com.poulastaa.core.database.repository.RedisLocalSessionCacheDatasource
import com.poulastaa.core.database.repository.auth.ExposedLocalAuthDatasource
import com.poulastaa.core.database.repository.auth.RedisLocalAuthDataSource
import com.poulastaa.core.database.repository.item.ExposedLocalItemDatasource
import com.poulastaa.core.database.repository.item.RedisLocalItemDatasource
import com.poulastaa.core.database.repository.search.ExposedLocalPagingDatasource
import com.poulastaa.core.database.repository.setup.ExposedSetupDatasource
import com.poulastaa.core.database.repository.setup.RedisSetupDatasource
import com.poulastaa.core.database.repository.suggestion.ExposedLocalSuggestionDatasource
import com.poulastaa.core.database.repository.suggestion.RedisLocalSuggestionDatasource
import com.poulastaa.core.database.repository.sync.ExposedLocalSyncDatasource
import com.poulastaa.core.database.repository.sync.RedisLocalSyncDatasource
import com.poulastaa.core.database.repository.view.ExposedLocalViewDatasource
import com.poulastaa.core.database.repository.view.RedisLocalViewDatasource
import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.LocalCoreDatasource
import com.poulastaa.core.domain.repository.auth.LocalAuthCacheDatasource
import com.poulastaa.core.domain.repository.auth.LocalAuthDatasource
import com.poulastaa.core.domain.repository.auth.LocalSessionCacheDatasource
import com.poulastaa.core.domain.repository.item.LocalItemCacheDatasource
import com.poulastaa.core.domain.repository.item.LocalItemDatasource
import com.poulastaa.core.domain.repository.search.LocalPagingDatasource
import com.poulastaa.core.domain.repository.setup.LocalSetupCacheDatasource
import com.poulastaa.core.domain.repository.setup.LocalSetupDatasource
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionCacheDatasource
import com.poulastaa.core.domain.repository.suggestion.LocalSuggestionDatasource
import com.poulastaa.core.domain.repository.sync.LocalSyncCacheDatasource
import com.poulastaa.core.domain.repository.sync.LocalSyncDatasource
import com.poulastaa.core.domain.repository.view.LocalViewCacheDatasource
import com.poulastaa.core.domain.repository.view.LocalViewDatasource
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

    single<LocalSetupCacheDatasource> {
        RedisSetupDatasource(
            gson = get(),
            redisPool = get(),
            core = get()
        )
    }

    single<LocalSetupDatasource> {
        ExposedSetupDatasource(
            coreDB = get(),
            cache = get()
        )
    }

    single<LocalSessionCacheDatasource> {
        RedisLocalSessionCacheDatasource(redisPool = get())
    }

    single<LocalSuggestionCacheDatasource> {
        RedisLocalSuggestionDatasource(
            gson = get(),
            redisPool = get(),
            core = get()
        )
    }

    single<LocalSuggestionDatasource> {
        ExposedLocalSuggestionDatasource(
            core = get(),
            cache = get()
        )
    }

    single<LocalViewCacheDatasource> {
        RedisLocalViewDatasource(
            gson = get(),
            redisPool = get(),
            core = get()
        )
    }

    single<LocalViewDatasource> {
        ExposedLocalViewDatasource(
            core = get(),
            cache = get()
        )
    }

    single<LocalSyncCacheDatasource> {
        RedisLocalSyncDatasource(get())
    }

    single<LocalSyncDatasource> {
        ExposedLocalSyncDatasource(get(), get())
    }

    single<LocalPagingDatasource> {
        ExposedLocalPagingDatasource()
    }

    single<LocalItemCacheDatasource> {
        RedisLocalItemDatasource(get())
    }

    single<LocalItemDatasource> {
        ExposedLocalItemDatasource(get(), get())
    }
}