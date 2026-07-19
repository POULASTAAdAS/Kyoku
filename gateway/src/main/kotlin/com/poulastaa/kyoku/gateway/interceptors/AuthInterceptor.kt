package com.poulastaa.kyoku.gateway.interceptors

import com.poulastaa.kyoku.gateway.model.ServiceConfigPayload
import com.poulastaa.kyoku.gateway.utils.dedupeAllCorsHeaders
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthInterceptor {
    @Bean
    fun authRequestInterceptor(
        builder: RouteLocatorBuilder,
        @Qualifier("provideAuthServicePayload")
        service: ServiceConfigPayload,
    ) = builder.routes()
        .route(service.id) { r ->
            r.path("${service.path}email/create-account").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.path("${service.path}email/login").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.path("${service.path}google/join").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.path("${service.path}forgot-password").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.path("${service.path}reset-password").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .build()!!
}
