package com.poulastaa.kyoku.playlist.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class AppConfig {
    @Bean
    fun provideRedis(factory: RedisConnectionFactory) = RedisTemplate<String, Any>().apply {
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(Any::class.java)

        this.connectionFactory = factory
        this.keySerializer = StringRedisSerializer()
        this.valueSerializer = jackson2JsonRedisSerializer
        this.hashKeySerializer = StringRedisSerializer()
        this.hashValueSerializer = jackson2JsonRedisSerializer
        this.afterPropertiesSet()
    }
}