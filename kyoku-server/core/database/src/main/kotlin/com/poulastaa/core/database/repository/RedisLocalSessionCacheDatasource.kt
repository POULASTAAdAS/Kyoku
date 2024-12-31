package com.poulastaa.core.database.repository

import com.poulastaa.core.domain.repository.auth.LocalSessionCacheDatasource
import com.poulastaa.core.domain.utils.Constants
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.SetParams

class RedisLocalSessionCacheDatasource(
    private val redisPool: JedisPool,
) : LocalSessionCacheDatasource {
    private object Group {
        const val SESSION = "SESSION"
    }

    override suspend fun write(id: String, value: String) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.SESSION}:$id",
                value,
                SetParams.setParams().nx().ex(Constants.DEFAULT_SESSION_MAX_AGE)
            )
        }
    }

    override suspend fun invalidate(id: String) {
        redisPool.resource.use { jedis ->
            jedis.del("${Group.SESSION}:$id")
        }
    }

    override suspend fun read(id: String): String? = redisPool.resource.use { jedis ->
        jedis.get("${Group.SESSION}:$id")
    }
}