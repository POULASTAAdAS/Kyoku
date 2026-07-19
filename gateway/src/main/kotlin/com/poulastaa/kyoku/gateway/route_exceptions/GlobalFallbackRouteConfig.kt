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
    @Order(Int.MAX_VALUE) // Lowest priority - will be matched last
    fun globalFallbackRoute(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route("global-fallback-route") { r ->
                r.order(Int.MAX_VALUE)
                    .path("/**") // Catch all extra routes
                    .filters { f ->
                        f.filter { exchange, _ ->
                            val path = exchange.request.uri.path

                            val errorWrapper = ResponseWrapper(
                                status = CustomResponseStatus.FUCK_YOU,
                                payload = mapOf(
                                    "message" to "Man your mom felt so good last night.",
                                    "path" to "you can take this back ASSHOLE: $path",
                                    "with love" to "try again fucker.\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣾⠟⠛⠛⠿⣷⣦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⠋⠀⠀⠀⠀⢸⣿⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⠀⠀⠀⠀⠀⠀⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⡿⠀⠀⠀⠀⠀⠰⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⡇⠀⠀⠀⠀⠀⠨⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⡇⠀⠀⠀⠀⠀⠈⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⡇⠀⠀⠀⠀⠀⢐⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⡇⠀⠀⠀⠀⠀⠠⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⡇⠀⠀⠀⠀⠀⢸⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣿⡇⠀⠀⠀⠀⠀⢰⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣾⡿⠿⢿⣿⣿⠃⠀⠀⠀⠀⠀⢸⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⢀⣾⡿⠋⠀⠀⠀⠹⣿⠀⠀⠀⠀⠀⠀⢸⣿⣷⣶⣦⣄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⣼⣿⠃⠀⠀⠀⠀⠀⣿⠀⠀⠀⠀⠀⠀⠈⣿⠛⠉⠉⠙⢿⣧⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⢀⣼⣿⡿⠀⠀⠀⠀⠀⠀⣿⠀⠀⠀⠀⠀⠀⠀⣿⠀⠀⠀⠀⠀⢹⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⢀⣴⣿⠿⣿⠇⠀⠀⠀⠀⠀⠀⣿⠀⠀⠀⠀⠀⠀⠀⣿⠀⠀⠀⠀⠀⠘⣿⠛⠲⢦⣤⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⣰⣿⠟⠁⠀⣿⠀⠀⠀⠀⠀⠀⢸⣿⠀⠀⠀⠀⠀⠀⠀⣿⠀⠀⠀⠀⠀⠀⣿⡆⠀⠀⠹⣧⡀⠀⠀⠀\n" +
                                            "⢀⣼⠟⠅⠀⠀⢸⣿⠀⠀⠀⠀⠀⠀⠸⠟⠁⠀⠀⠀⠀⠀⠑⠿⠃⠀⠀⠀⠀⠀⢹⣇⠀⠀⠀⠹⣧⠀⠀⠀\n" +
                                            "⣾⠏⠀⠀⠀⠀⢸⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⡆⠀⠀⠀⢽⣧⠀⠀\n" +
                                            "⣿⠀⠀⠀⠀⠀⣸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠛⠃⠀⠀⠀⠀⣿⣆⠀\n" +
                                            "⣿⣆⠀⠀⠀⠀⠙⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⡀\n" +
                                            "⠹⣿⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⡇\n" +
                                            "⠀⠈⢻⣿⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣿⠇\n" +
                                            "⠀⠀⠀⠙⢿⣿⣆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⡟⠀\n" +
                                            "⠀⠀⠀⠀⠀⠙⣿⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⣿⠟⠁⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠈⠹⣿⣷⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣾⡿⠋⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣿⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣴⣿⠏⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣿⠃⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⠇⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⡇⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠻⣿⣶⣶⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣶⣶⣶⣿⠿⠃⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                                            "                                              "
                                )
                            )

                            exchange.writeResponseWrapper(
                                statusCode = HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS,
                                responseWrapper = errorWrapper,
                                mapper = mapper
                            )
                        }
                    }.uri("no://op") // No operation - route won't be forwarded
            }.build()
    }
}
