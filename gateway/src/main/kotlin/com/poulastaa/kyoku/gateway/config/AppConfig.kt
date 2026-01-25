package com.poulastaa.kyoku.gateway.config

import com.poulastaa.kyoku.gateway.model.ServiceConfigPayload
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun provideAuthServicePayload(
        @Value("\${spring.cloud.gateway.server.webflux.routes[2].id}")
        name: String,
        @Value("\${spring.cloud.gateway.server.webflux.routes[2].uri}")
        uri: String,
        @Value("\${spring.cloud.gateway.server.webflux.routes[2].predicates[0]}")
        servicePath: String,
    ) = ServiceConfigPayload(
        id = name,
        uri = uri,
        servicePath = servicePath
    )

    @Bean
    fun provideRequestValidatorServicePayload(
        @Value("\${spring.cloud.gateway.server.webflux.routes[2].id}")
        name: String,
        @Value("\${spring.cloud.gateway.server.webflux.routes[2].uri}")
        uri: String,
        @Value("\${spring.cloud.gateway.server.webflux.routes[2].predicates[0]}")
        servicePath: String,
    ) = ServiceConfigPayload(
        id = name,
        uri = uri,
        servicePath = servicePath
    )

    // grpc
    @Bean
    fun providePlaylistServicePayload(
        @Value("\${spring.cloud.gateway.server.webflux.routes[4].id}")
        name: String,
        @Value("\${spring.cloud.gateway.server.webflux.routes[4].uri}")
        uri: String,
        @Value("\${spring.cloud.gateway.server.webflux.routes[4].predicates[0]}")
        servicePath: String,
    ) = ServiceConfigPayload(
        id = name,
        uri = uri,
        servicePath = servicePath
    )

    // grpc
    @Bean
    fun provideUserServicePayload(
        @Value("\${spring.cloud.gateway.server.webflux.routes[5].id}")
        name: String,
        @Value("\${spring.cloud.gateway.server.webflux.routes[5].uri}")
        uri: String,
        @Value("\${spring.cloud.gateway.server.webflux.routes[5].predicates[0]}")
        servicePath: String,
    ) = ServiceConfigPayload(
        id = name,
        uri = uri,
        servicePath = servicePath
    )

    // grpc
    @Bean
    fun provideContentServicePayload(
        @Value("\${spring.cloud.gateway.server.webflux.routes[6].id}")
        name: String,
        @Value("\${spring.cloud.gateway.server.webflux.routes[6].uri}")
        uri: String,
        @Value("\${spring.cloud.gateway.server.webflux.routes[6].predicates[0]}")
        servicePath: String,
    ) = ServiceConfigPayload(
        id = name,
        uri = uri,
        servicePath = servicePath
    )
}