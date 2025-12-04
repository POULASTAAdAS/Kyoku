package com.poulastaa.kyoku.validator.config

import com.poulastaa.kyoku.validator.model.dto.DtoJWTConfigInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
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
}