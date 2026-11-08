package com.poulastaa.kyoku.gateway.interceptors.song

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.protobuf.util.JsonFormat
import com.poulastaa.kyoku.gateway.interceptors.ValidationFilter
import com.poulastaa.kyoku.gateway.interceptors.wrapResponse
import com.poulastaa.kyoku.gateway.interceptors.writeResponseWrapper
import com.poulastaa.kyoku.gateway.model.ServiceConfigPayload
import com.poulastaa.kyoku.gateway.model.dto.DtoAuthenticationTokenClaim
import com.poulastaa.kyoku.gateway.model.response.CustomResponseStatus
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
import com.poulastaa.kyoku.gateway.utils.NonRetryableAuthenticationException
import com.poulastaa.kyoku.grpc.gateway_song.GatewaySongServiceGrpc
import com.poulastaa.kyoku.grpc.gateway_song.RequestSongStreamUrl
import io.grpc.Status
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.reactor.mono
import net.devh.boot.grpc.client.inject.GrpcClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ServerWebExchange
import java.util.concurrent.TimeUnit

// read only = one CONTENT db lookup + local HMAC
private const val STREAM_URL_DEADLINE_SECONDS = 5L

@Configuration
class SongRouteConfig {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GrpcClient("content")
    private lateinit var songStub: GatewaySongServiceGrpc.GatewaySongServiceFutureStub

    private val song: GatewaySongServiceGrpc.GatewaySongServiceFutureStub
        get() = songStub.withDeadlineAfter(STREAM_URL_DEADLINE_SECONDS, TimeUnit.SECONDS)

    @Bean
    fun provideSongStreamUrlRoute(
        builder: RouteLocatorBuilder,
        @Qualifier("provideContentServicePayload") service: ServiceConfigPayload,
        validationFilter: ValidationFilter,
        mapper: ObjectMapper,
    ) = builder.routes()
        .route(service.id) { r ->
            r.path("${service.path}song/stream-url")
                .and()
                .method(HttpMethod.GET)
                .filters { f ->
                    f.filter(validationFilter)

                    f.filter { exchange, _ ->
                        mono(Dispatchers.IO) {
                            val user = exchange.authenticatedUser()

                            val songId = exchange.request.queryParams["songId"]?.firstOrNull()?.toLongOrNull()
                                ?.takeIf { it > 0 }
                                ?: return@mono ResponseEntity(
                                    ResponseWrapper(
                                        status = CustomResponseStatus.INVALID_REQUEST_BODY,
                                        payload = "Missing songId"
                                    ),
                                    HttpStatus.BAD_REQUEST
                                )

                            val response = song.getStreamUrl(
                                RequestSongStreamUrl.newBuilder().apply {
                                    this.songId = songId
                                    this.email = user.email
                                }.build()
                            ).await()

                            val jsonString = JsonFormat.printer()
                                .includingDefaultValueFields()
                                .print(response)

                            ResponseEntity(jsonString, HttpStatus.OK)
                        }.wrapResponse(exchange, mapper).onErrorResume { error ->
                            val (httpStatus, responseStatus) = error.toResponseStatus()

                            if (httpStatus.is5xxServerError) logger.error("Error in SongInterceptor", error)
                            else logger.info("Song stream url rejected: {}", error.message)

                            exchange.writeResponseWrapper(
                                statusCode = httpStatus,
                                responseWrapper = ResponseWrapper(
                                    status = responseStatus,
                                    payload = responseStatus.message
                                ),
                                mapper = mapper
                            )
                        }
                    }
                }
                .uri(service.uri)
        }.build()!!

    private fun ServerWebExchange.authenticatedUser() =
        attributes[ValidationFilter.AUTHENTICATED_USER_KEY] as? DtoAuthenticationTokenClaim
            ?: throw NonRetryableAuthenticationException(
                "User context missing",
                HttpStatus.UNAUTHORIZED
            )

    private fun Throwable.toResponseStatus(): Pair<HttpStatus, CustomResponseStatus> {
        if (this is NonRetryableAuthenticationException) return HttpStatus.UNAUTHORIZED to CustomResponseStatus.UNAUTHORIZED

        val code = (this as? StatusRuntimeException)?.status?.code
            ?: (cause as? StatusRuntimeException)?.status?.code

        return when (code) {
            Status.Code.INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST to CustomResponseStatus.INVALID_REQUEST_BODY
            Status.Code.NOT_FOUND -> HttpStatus.NOT_FOUND to CustomResponseStatus.NOT_FOUND
            Status.Code.UNAVAILABLE,
            Status.Code.DEADLINE_EXCEEDED,
                -> HttpStatus.SERVICE_UNAVAILABLE to CustomResponseStatus.SERVICE_UNAVAILABLE

            else -> HttpStatus.INTERNAL_SERVER_ERROR to CustomResponseStatus.INTERNAL_SERVER_ERROR
        }
    }
}
