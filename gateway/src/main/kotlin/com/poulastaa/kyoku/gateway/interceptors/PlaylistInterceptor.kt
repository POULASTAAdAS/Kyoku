package com.poulastaa.kyoku.gateway.interceptors

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.protobuf.util.JsonFormat
import com.poulastaa.kyoku.gateway.model.DtoUser
import com.poulastaa.kyoku.gateway.model.ServiceConfigPayload
import com.poulastaa.kyoku.gateway.model.UserType
import com.poulastaa.kyoku.gateway.model.response.ResponseStatus
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
import com.poulastaa.kyoku.gateway.utils.NonRetryableAuthenticationException
import com.poulastaa.kyoku.grpc.gateway_playlist.GatewayPlaylistServiceGrpc
import com.poulastaa.kyoku.grpc.gateway_playlist.RequestGetPlaylist
import com.poulastaa.kyoku.grpc.gateway_playlist.RequestUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.reactor.mono
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono

@Configuration
class PlaylistRouteConfig {

    @GrpcClient("playlist")
    // Use CoroutineStub
    private lateinit var playlist: GatewayPlaylistServiceGrpc.GatewayPlaylistServiceFutureStub

    @Bean
    fun providePlaylistRoute(
        builder: RouteLocatorBuilder,
        @Qualifier("providePlaylistServicePayload") service: ServiceConfigPayload,
        validationFilter: ValidationFilter, // Inject the corrected filter
        mapper: ObjectMapper,
    ) = builder.routes()
        .route(service.id) { r ->
            r.path("${service.path}test")
                .filters { f ->
                    // 1. Add Validation Filter
                    f.filter(validationFilter)

                    // 2. Add Custom Logic Filter
                    f.filter { exchange, _ ->
                        mono(Dispatchers.IO) {
                            val user = exchange.attributes[ValidationFilter.AUTHENTICATED_USER_KEY] as? DtoUser
                                ?: throw NonRetryableAuthenticationException(
                                    "User context missing",
                                    HttpStatus.UNAUTHORIZED
                                )

                            val playlistId = exchange.request.queryParams["playlistId"]?.firstOrNull()
                                ?: return@mono ResponseEntity(
                                    ResponseWrapper(
                                        status = ResponseStatus.UNAUTHORIZED,
                                        payload = "Missing playlistId"
                                    ),
                                    HttpStatus.BAD_REQUEST
                                )

                            val request = RequestGetPlaylist.newBuilder().apply {
                                this.playlistId = playlistId
                                this.user = RequestUser.newBuilder().apply {
                                    email = user.email
                                    type = when (user.type) {
                                        UserType.EMAIL -> RequestUser.UserType.EMAIL
                                        UserType.GOOGLE -> RequestUser.UserType.GOOGLE
                                    }
                                }.build()
                            }.build()

                            // Suspend call (non-blocking)
                            val response = playlist.getPlaylist(request).await()

                            // Convert to JSON
                            val jsonString = JsonFormat.printer()
                                .includingDefaultValueFields()
                                .print(response)

                            // Return raw JSON string with 200 OK
                            ResponseEntity(jsonString, HttpStatus.OK)
                        }.flatMap { responseEntity ->
                            // Write the ResponseEntity to the ServerWebExchange
                            val response = exchange.response
                            response.statusCode = responseEntity.statusCode
                            response.headers.contentType = MediaType.APPLICATION_JSON

                            val bytes = when (val body = responseEntity.body) {
                                is String -> body.toByteArray() // It's the JSON string from Proto
                                is ResponseWrapper<*> -> mapper.writeValueAsBytes(body) // It's an error object
                                else -> ByteArray(0)
                            }

                            response.writeWith(
                                Mono.just(response.bufferFactory().wrap(bytes))
                            )
                        }
                    }
                }
                .uri(service.uri)
        }.build()!!
}