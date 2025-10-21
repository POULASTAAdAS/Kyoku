package com.poulastaa.kyoku.gateway.interceptors

import com.poulastaa.kyoku.gateway.model.ServiceConfigPayload
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono

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
                    .modifyResponseBody(
                        ResponseWrapper::class.java,
                        Any::class.java,
                    ) { _, response ->
                        if (response.payload != null) Mono.just(response.payload)
                        else Mono.just(
                            ResponseWrapper(
                                status = response.status,
                                payload = response.status,
                            )
                        )
                    }
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.path("${service.path}email/login").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .modifyResponseBody(
                        ResponseWrapper::class.java,
                        Any::class.java,
                    ) { _, response ->

                        if (response.payload != null) Mono.just(response.payload)
                        else Mono.just(
                            ResponseWrapper(
                                status = response.status,
                                payload = response.status,
                            )
                        )
                    }
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.path("${service.path}forgot-password").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .modifyResponseBody(
                        ResponseWrapper::class.java,
                        Any::class.java,
                    ) { _, response ->

                        if (response.payload != null) Mono.just(response.payload)
                        else Mono.just(
                            ResponseWrapper(
                                status = response.status,
                                payload = response.status,
                            )
                        )
                    }
            }.uri(service.uri)
        }
        .route(service.id) { r ->
            r.path("${service.path}reset-password").filters { f ->
                f.dedupeAllCorsHeaders() // todo: add retry and circuit breaker
                    .modifyResponseBody(
                        ResponseWrapper::class.java,
                        Any::class.java,
                    ) { _, response ->

                        if (response.payload != null) Mono.just(response.payload)
                        else Mono.just(
                            ResponseWrapper(
                                status = response.status,
                                payload = response.status,
                            )
                        )
                    }
            }.uri(service.uri)
        }
        .build()!!


    /**
     * This function removes duplicate CORS (Cross-Origin Resource Sharing) headers from HTTP responses.
     * When multiple services or filters add CORS headers, duplicates can occur which may cause issues with browsers are removed.
     *
     * Uses Spring Cloud Gateway's dedupeResponseHeader() method with the "RETAIN_FIRST" strategy:
     *
     *      -> Access-Control-Allow-Origin - specifies allowed origins
     *      -> Access-Control-Allow-Credentials - indicates if credentials are allowed
     *      -> Access-Control-Allow-Methods - specifies allowed HTTP methods
     *      -> Access-Control-Allow-Headers - specifies allowed request headers
     *      -> Access-Control-Max-Age - specifies how long preflight results can be cached
     *      -> Access-Control-Expose-Headers - specifies headers that can be exposed to the client
     *      -> Vary - indicates which headers affect caching
     */
    private fun GatewayFilterSpec.dedupeAllCorsHeaders() = this
        .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_FIRST")
        .dedupeResponseHeader("Access-Control-Allow-Credentials", "RETAIN_FIRST")
        .dedupeResponseHeader("Access-Control-Allow-Methods", "RETAIN_FIRST")
        .dedupeResponseHeader("Access-Control-Allow-Headers", "RETAIN_FIRST")
        .dedupeResponseHeader("Access-Control-Max-Age", "RETAIN_FIRST")
        .dedupeResponseHeader("Access-Control-Expose-Headers", "RETAIN_FIRST")
        .dedupeResponseHeader("Vary", "RETAIN_FIRST")

}