package com.poulastaa.core.database.repository.setup

import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.setup.LocalSetupCacheDatasource

class RedisSetupDatasource(
    private val core: LocalCoreCacheDatasource
): LocalSetupCacheDatasource {
}