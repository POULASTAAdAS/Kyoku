package com.poulastaa.kyoku.auth.config

import com.poulastaa.kyoku.auth.model.dto.DtoJWTConfigInfo
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
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

    @Bean
    fun provideVerificationMailConfigurationsClass(
        @Value("\${jwt.mail.verify.secret}")
        secret: String,
        @Value("\${jwt.mail.verify.subject}")
        subject: String,
        @Value("\${jwt.mail.verify.issuer}")
        issuer: String,
        @Value("\${jwt.mail.verify.audience}")
        audience: String,
        @Value("\${jwt.mail.verify.time}")
        expTime: Int,
        @Value("\${jwt.mail.verify.unit}")
        unit: String = "MINUTES",
        @Value("\${jwt.mail.verify.claim.key}")
        claimKey: String,
    ) = DtoJWTConfigInfo(
        secret = secret,
        subject = subject,
        issuer = issuer,
        audience = audience,
        expiresIn = expTime,
        unit = unit,
        claimKey = claimKey,
    )

    @Bean
    fun provideUpdatePasswordTokenConfigurationsClass(
        @Value("\${jwt.update-password.secret}")
        secret: String,
        @Value("\${jwt.update-password.subject}")
        subject: String,
        @Value("\${jwt.update-password.issuer}")
        issuer: String,
        @Value("\${jwt.update-password.audience}")
        audience: String,
        @Value("\${jwt.update-password.time}")
        expTime: Int,
        @Value("\${jwt.update-password.unit}")
        unit: String = "MINUTES",
        @Value("\${jwt.update-password.claim.key}")
        claimKey: String,
    ) = DtoJWTConfigInfo(
        secret = secret,
        subject = subject,
        issuer = issuer,
        audience = audience,
        expiresIn = expTime,
        unit = unit,
        claimKey = claimKey,
    )

    @Bean
    fun provideAccessTokenConfigurationsClass(
        @Value("\${jwt.access.secret}")
        secret: String,
        @Value("\${jwt.access.subject}")
        subject: String,
        @Value("\${jwt.access.issuer}")
        issuer: String,
        @Value("\${jwt.access.audience}")
        audience: String,
        @Value("\${jwt.access.time}")
        expTime: Int,
        @Value("\${jwt.access.unit}")
        unit: String = "HOURS",
        @Value("\${jwt.access.claim.key}")
        claimKey: String,
    ) = DtoJWTConfigInfo(
        secret = secret,
        subject = subject,
        issuer = issuer,
        audience = audience,
        expiresIn = expTime,
        unit = unit,
        claimKey = claimKey,
    )

    @Bean
    fun provideRefreshTokenConfigurationsClass(
        @Value("\${jwt.refresh.secret}")
        secret: String,
        @Value("\${jwt.refresh.subject}")
        subject: String,
        @Value("\${jwt.refresh.issuer}")
        issuer: String,
        @Value("\${jwt.refresh.audience}")
        audience: String,
        @Value("\${jwt.refresh.time}")
        expTime: Int,
        @Value("\${jwt.refresh.unit}")
        unit: String = "MINUTES",
        @Value("\${jwt.refresh.claim.key}")
        claimKey: String,
    ) = DtoJWTConfigInfo(
        secret = secret,
        subject = subject,
        issuer = issuer,
        audience = audience,
        expiresIn = expTime,
        unit = unit,
        claimKey = claimKey,
    )
}