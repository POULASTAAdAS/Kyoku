package com.poulastaa.kyoku.gateway.route_exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import com.poulastaa.kyoku.gateway.interceptors.writeResponseWrapper
import com.poulastaa.kyoku.gateway.model.response.CustomResponseStatus
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus

@Configuration
class GlobalFallbackRouteConfig(
    private val mapper: ObjectMapper,
) {
    @Bean
    @Order(Int.MAX_VALUE)
    fun globalFallbackRoute(builder: RouteLocatorBuilder): RouteLocator = builder.routes()
        .route("global-fallback-route") { r ->
            r.order(Int.MAX_VALUE)
                .path("/**")
                .filters { f ->
                    f.filter { exchange, _ ->
                        exchange.writeResponseWrapper(
                            statusCode = HttpStatus.NOT_FOUND,
                            responseWrapper = ResponseWrapper<Unit>(
                                status = CustomResponseStatus.NOT_FOUND,
                            ),
                            mapper = mapper,
                        )
                    }
                }
                .uri("no://op")
        }.build()
}
