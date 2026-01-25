package com.poulastaa.kyoku.content.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Bean
    fun provideRedis(factory: RedisConnectionFactory) = RedisTemplate<String, Any>().apply {
        val genericSerializer = GenericJackson2JsonRedisSerializer()

        this.connectionFactory = factory
        this.keySerializer = StringRedisSerializer()
        this.valueSerializer = genericSerializer
        this.hashKeySerializer = StringRedisSerializer()
        this.hashValueSerializer = genericSerializer
        this.afterPropertiesSet()
    }
}