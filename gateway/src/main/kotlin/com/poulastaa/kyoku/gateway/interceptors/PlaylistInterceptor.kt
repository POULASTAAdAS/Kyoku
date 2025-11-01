package com.poulastaa.kyoku.gateway.interceptors

import com.poulastaa.kyoku.gateway.model.ServiceConfigPayload
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PlaylistInterceptor {
    @Bean
    fun providePlaylistInterceptor(
        builder: RouteLocatorBuilder,
        @Qualifier("provideRequestValidatorServicePayload")
        service: ServiceConfigPayload,
    ) = builder.routes()
        .route {
            // validate token from request-validator http request

            // get playlist-service from eureka

            // convert to grpc and send to playlist-service

            TODO()
        }.build()!!
}