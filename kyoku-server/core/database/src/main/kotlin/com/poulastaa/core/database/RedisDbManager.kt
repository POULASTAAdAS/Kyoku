package com.poulastaa.core.database

import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

object RedisDbManager {
    fun jedisPool(): JedisPool {
        val redisHost = System.getenv("redisHost") ?: "localhost"
        val redisPort = System.getenv("redisPort")?.toInt() ?: 6380
        val redisPassword = System.getenv("redisPassword") ?: "redisPassword"
        val timeout = 2000

        val jedisPool = JedisPool(JedisPoolConfig(), redisHost, redisPort, timeout, redisPassword)

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