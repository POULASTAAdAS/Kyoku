package com.poulastaa.app.di

import org.koin.dsl.module
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

fun provideJedisPool() = module {
    single<JedisPool> {
        jedisPool()
    }
}

private fun jedisPool(): JedisPool {
    val redisHost = System.getenv("redisHost") ?: "localhost"
    val redisPort = 6380
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