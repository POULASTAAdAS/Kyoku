package com.poulastaa.kyoku.gateway.interceptors

import com.fasterxml.jackson.databind.ObjectMapper
import com.poulastaa.kyoku.gateway.model.ServiceConfigPayload
import com.poulastaa.kyoku.gateway.model.response.CustomResponseStatus
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
import com.poulastaa.kyoku.gateway.utils.dedupeAllCorsHeaders
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus

@Configuration
class AuthInterceptor(
    private val mapper: ObjectMapper,
) {
    @Bean
    fun authRequestInterceptor(
        builder: RouteLocatorBuilder,
        @Qualifier("provideAuthServicePayload")
        service: ServiceConfigPayload,
    ) = builder.routes()
        .route(service.id) { r ->
            r.order(AUTH_ROUTE_ORDER).path("${service.path}email/create-account").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.order(AUTH_ROUTE_ORDER).path("${service.path}email/login").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.order(AUTH_ROUTE_ORDER).path("${service.path}google/join").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.order(AUTH_ROUTE_ORDER).path("${service.path}email/verify-email").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.order(AUTH_ROUTE_ORDER).path("${service.path}email/verify-email/state").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.order(AUTH_ROUTE_ORDER).path("${service.path}refresh-token").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.order(AUTH_ROUTE_ORDER).path("${service.path}email/forgot-password").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.order(AUTH_ROUTE_ORDER).path("${service.path}email/forgot-password/validate").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.order(AUTH_ROUTE_ORDER).path("${service.path}email/reset-password").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .wrapResponseWrapperBody()
            }.uri(service.uri)
        }
        .route("${service.id}-fallback") { r ->
            r.order(AUTH_FALLBACK_ROUTE_ORDER).path("${service.path}**").filters { f ->
                f.filter { exchange, _ ->
                    exchange.writeResponseWrapper(
                        statusCode = HttpStatus.NOT_FOUND,
                        responseWrapper = ResponseWrapper<Unit>(
                            status = CustomResponseStatus.NOT_FOUND,
                        ),
                        mapper = mapper,
                    )
                }
            }.uri("no://op")
        }
        .build()!!

    private companion object {
        const val AUTH_ROUTE_ORDER = -2
        const val AUTH_FALLBACK_ROUTE_ORDER = -1
    }
}
