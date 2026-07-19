package com.poulastaa.kyoku.gateway.interceptors.setup

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.protobuf.util.JsonFormat
import com.poulastaa.kyoku.gateway.interceptors.ValidationFilter
import com.poulastaa.kyoku.gateway.interceptors.wrapResponse
import com.poulastaa.kyoku.gateway.interceptors.writeResponseWrapper
import com.poulastaa.kyoku.gateway.model.ServiceConfigPayload
import com.poulastaa.kyoku.gateway.model.UserType
import com.poulastaa.kyoku.gateway.model.dto.DtoAuthenticationTokenClaim
import com.poulastaa.kyoku.gateway.model.response.CustomResponseStatus
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
import org.springframework.http.ResponseEntity
import java.util.concurrent.TimeUnit

@Configuration
class PlaylistRouteConfig {
    @GrpcClient("playlist")
    private lateinit var playlistStub: GatewayPlaylistServiceGrpc.GatewayPlaylistServiceFutureStub

    private val playlist: GatewayPlaylistServiceGrpc.GatewayPlaylistServiceFutureStub
        get() = playlistStub.withDeadlineAfter(8, TimeUnit.SECONDS)

    @Bean
    fun providePlaylistRoute(
        builder: RouteLocatorBuilder,
        @Qualifier("providePlaylistServicePayload") service: ServiceConfigPayload,
        validationFilter: ValidationFilter, // Inject the corrected filter
        mapper: ObjectMapper,
    ) = builder.routes()
        .route(service.id) { r ->
            r.path("${service.path}import_playlist")
                .filters { f ->
                    // 1. Add Validation Filter
                    f.filter(validationFilter)

                    // 2. Add Custom Logic Filter
                    f.filter { exchange, _ ->
                        mono(Dispatchers.IO) {
                            val user =
                                exchange.attributes[ValidationFilter.AUTHENTICATED_USER_KEY] as? DtoAuthenticationTokenClaim
                                    ?: throw NonRetryableAuthenticationException(
                                        "User context missing",
                                        HttpStatus.UNAUTHORIZED
                                    )

                            val playlistId = exchange.request.queryParams["playlistId"]?.firstOrNull()
                                ?: return@mono ResponseEntity(
                                    ResponseWrapper(
                                        status = CustomResponseStatus.UNAUTHORIZED,
                                        payload = "Missing playlistId"
                                    ),
                                    HttpStatus.BAD_REQUEST
                                )

                            val request = RequestGetPlaylist.newBuilder().apply {
                                this.playlistId = playlistId
                                this.user = RequestUser.newBuilder().apply {
                                    email = user.email
                                    type = when (user.userType) {
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
                        }.wrapResponse(exchange, mapper).onErrorResume { error ->
                            println("Error in PlaylistInterceptor: ${error.message}")
                            error.printStackTrace()

                            val response = exchange.response

                            val errorWrapper = ResponseWrapper(
                                status = when {
                                    error.message?.contains("NOT_FOUND", ignoreCase = true) == true -> {
                                        response.statusCode = HttpStatus.NOT_FOUND
                                        CustomResponseStatus.NO_CONTENT
                                    }

                                    error.message?.contains("CANCELLED", ignoreCase = true) == true -> {
                                        response.statusCode = HttpStatus.NO_CONTENT
                                        CustomResponseStatus.NO_CONTENT
                                    }

                                    else -> {
                                        response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
                                        CustomResponseStatus.INTERNAL_SERVER_ERROR
                                    }
                                },
                                payload = if (error.message?.contains(
                                        other = "DEADLINE_EXCEEDED",
                                        ignoreCase = true
                                    ) == true
                                ) CustomResponseStatus.INTERNAL_SERVER_ERROR.message else error.message
                            )

                            exchange.writeResponseWrapper(
                                statusCode = response.statusCode ?: HttpStatus.INTERNAL_SERVER_ERROR,
                                responseWrapper = errorWrapper,
                                mapper = mapper
                            )
                        }
                    }
                }
                .uri(service.uri)
        }.build()!!
}
