package com.poulastaa.core.database.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.LocalCacheDatasource
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.SetParams

class RedisLocalCacheDataSource(
    private val redisPool: JedisPool,
    private val gson: Gson,
) : LocalCacheDatasource {
    private object Group {
        const val COUNTRY_ID = "COUNTRY_ID"
        const val USER = "USER"
        const val EMAIL_VERIFICATION_STATUS = "EMAIL_VERIFICATION_STATUS"
    }

    override fun cachedCountryId(key: String): Int? = redisPool.resource.use { jedis ->
        jedis.get("${Group.COUNTRY_ID}:$key")?.toInt()
    }

    override fun setCountryId(key: String, value: String) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.COUNTRY_ID}:$key",
                value,
                SetParams.setParams().nx().ex(3600) // 1 hour
            )
        }
    }

    override fun cachedUserByEmail(
        key: String,
        type: UserType,
    ): DBUserDto? = redisPool.resource.use { jedis ->
        jedis.get("${Group.USER}:${type.name}:$key")
    }?.let { string ->
        gson.fromJson(string, DBUserDto::class.java)
    }

    override fun setUserByEmail(
        key: String,
        type: UserType,
        value: DBUserDto,
    ) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.USER}:${type.name}:$key",
                gson.toJson(value),
                SetParams.setParams().nx().ex(15 * 60) // 15 minute
            )
        }
    }

    override suspend fun cacheEmailVerificationStatus(key: Long): Boolean? = redisPool.resource.use { jedis ->
        jedis.get("${Group.EMAIL_VERIFICATION_STATUS}:$key")
    }?.toBoolean()

    override suspend fun setEmailVerificationStatus(key: Long, value: Boolean) {
        redisPool.resource.use { jedis ->
            jedis.set(
                "${Group.EMAIL_VERIFICATION_STATUS}:$key",
                value.toString(),
                SetParams.setParams().nx().ex(15 * 60) // 15 minute
            )
        }
    }
}