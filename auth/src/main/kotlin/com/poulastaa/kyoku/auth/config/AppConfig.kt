package com.poulastaa.kyoku.auth.config

import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class AppConfig {
    @Bean
    fun provideRabbitMQQueue() = RabbitQueues().init()

    @Bean
    fun provideRabbitMQTemplate(
        factory: ConnectionFactory,
    ) = RabbitTemplate(factory).apply {
        messageConverter = Jackson2JsonMessageConverter()
    }

    @Bean
    fun provideRedis(
        factory: RedisConnectionFactory,
    ) = RedisTemplate<String, Any>().apply {
        this.connectionFactory = factory
        this.keySerializer = StringRedisSerializer()
        this.valueSerializer = Jackson2JsonRedisSerializer(Any::class.java)
        setEnableTransactionSupport(true)
        afterPropertiesSet()
    }
}