package com.poulastaa.core.database

import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

object RedisDbManager {
    private var IS_INITIALIZED = false
    private const val TIME_OUT = 2000

    @Synchronized
    fun initializeRedisDatabases(
        redisHost: String,
        redisPort: Int,
        redisPassword: String,
    ): JedisPool {
        if (IS_INITIALIZED) throw IllegalStateException("Databases are already initialized!")
        IS_INITIALIZED = true

        val jedisPool = JedisPool(JedisPoolConfig(), redisHost, redisPort, TIME_OUT, redisPassword)

        try {
            jedisPool.resource.use { jedis ->
                if (jedis.ping() != "PONG") throw RuntimeException("Unable to connect to Redis")
            }
            println("Redis is running at $redisHost:$redisPort")
        } catch (e: Exception) {
            throw RuntimeException("Failed to initialize Redis pool: ${e.message}", e)
        }

        return jedisPool
    }
}