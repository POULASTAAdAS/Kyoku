package com.poulastaa.kyoku.search.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Bean
    fun provideRedis(factory: RedisConnectionFactory) = RedisTemplate<String, Any>().apply {
        val stringRedisSerializer = StringRedisSerializer()
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(Any::class.java)

        this.connectionFactory = factory
        this.keySerializer = stringRedisSerializer
        this.valueSerializer = jackson2JsonRedisSerializer

        this.hashKeySerializer = stringRedisSerializer
        this.hashValueSerializer = jackson2JsonRedisSerializer

        setEnableTransactionSupport(true)
        this.afterPropertiesSet()
    }
}