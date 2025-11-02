package com.poulastaa.kyoku.gateway.interceptors

import com.google.protobuf.util.JsonFormat
import com.poulastaa.kyoku.gateway.model.ServiceConfigPayload
import com.poulastaa.kyoku.grpc.gateway_playlist.GatewayPlaylistServiceGrpc
import com.poulastaa.kyoku.grpc.gateway_playlist.RequestGetPlaylist
import com.poulastaa.kyoku.grpc.gateway_playlist.RequestUser
import kotlinx.coroutines.reactor.mono
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import reactor.core.publisher.Mono

@Configuration
class PlaylistInterceptor {
    @GrpcClient("playlist")
    private lateinit var playlist: GatewayPlaylistServiceGrpc.GatewayPlaylistServiceBlockingStub

    @Bean
    fun providePlaylistInterceptor(
        builder: RouteLocatorBuilder,
        @Qualifier("providePlaylistServicePayload")
        service: ServiceConfigPayload,
    ) = builder.routes()
        .route(service.id) { r ->
            r.path("${service.path}test")
                .filters { f ->
                    f.filter { exchange, _ ->
                        // Create the gRPC request
                        val request = RequestGetPlaylist.newBuilder().apply {
                            playlistId = "2382397j8u8"
                            user = RequestUser.newBuilder().apply {
                                email = "poulastaadas2@gmail.com"
                                type = RequestUser.UserType.EMAIL
                            }.build()
                        }.build()

                        // Use mono {} to wrap the blocking gRPC call
                        mono {
                            playlist.getPlaylist(request)
                        }.flatMap { response ->
                            // Convert protobuf response to JSON
                            val jsonResponse = JsonFormat.printer()
                                .includingDefaultValueFields()
                                .print(response)

                            // Set response headers and write JSON
                            val httpResponse = exchange.response
                            httpResponse.headers.contentType = MediaType.APPLICATION_JSON

                            httpResponse.writeWith(
                                Mono.just(
                                    httpResponse.bufferFactory().wrap(jsonResponse.toByteArray())
                                )
                            )
                        }
                    }
                }
                .uri(service.uri)
        }.build()!!
}