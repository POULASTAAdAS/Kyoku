package com.poulastaa.kyoku.gateway.interceptors.setup

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.google.protobuf.util.JsonFormat
import com.poulastaa.kyoku.gateway.interceptors.ValidationFilter
import com.poulastaa.kyoku.gateway.interceptors.wrapResponse
import com.poulastaa.kyoku.gateway.interceptors.writeResponseWrapper
import com.poulastaa.kyoku.gateway.model.ServiceConfigPayload
import com.poulastaa.kyoku.gateway.model.UserType
import com.poulastaa.kyoku.gateway.model.dto.DtoAuthenticationTokenClaim
import com.poulastaa.kyoku.gateway.model.request.ApiRequestImportPlaylist
import com.poulastaa.kyoku.gateway.model.response.CustomResponseStatus
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
import com.poulastaa.kyoku.gateway.utils.NonRetryableAuthenticationException
import com.poulastaa.kyoku.gateway.utils.RequestBodyExtractor
import com.poulastaa.kyoku.grpc.gateway_playlist.GatewayPlaylistServiceGrpc
import com.poulastaa.kyoku.grpc.gateway_playlist.RequestImportPlaylist
import com.poulastaa.kyoku.grpc.gateway_playlist.RequestUser
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
import java.util.concurrent.TimeUnit

// import = spotify paging + user lookup + search + db write: keep above the playlist service's internal deadlines
private const val IMPORT_PLAYLIST_DEADLINE_SECONDS = 30L

@Configuration
class PlaylistRouteConfig {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GrpcClient("playlist")
    private lateinit var playlistStub: GatewayPlaylistServiceGrpc.GatewayPlaylistServiceFutureStub

    private val playlist: GatewayPlaylistServiceGrpc.GatewayPlaylistServiceFutureStub
        get() = playlistStub.withDeadlineAfter(IMPORT_PLAYLIST_DEADLINE_SECONDS, TimeUnit.SECONDS)

    @Bean
    fun providePlaylistRoute(
        builder: RouteLocatorBuilder,
        @Qualifier("providePlaylistServicePayload") service: ServiceConfigPayload,
        validationFilter: ValidationFilter,
        mapper: ObjectMapper,
    ) = builder.routes()
        .route(service.id) { r ->
            r.path("${service.path}import")
                .and()
                .method(HttpMethod.POST)
                .filters { f ->
                    f.filter(validationFilter)

                    f.filter { exchange, _ ->
                        mono(Dispatchers.IO) {
                            val user =
                                exchange.attributes[ValidationFilter.AUTHENTICATED_USER_KEY] as? DtoAuthenticationTokenClaim
                                    ?: throw NonRetryableAuthenticationException(
                                        "User context missing",
                                        HttpStatus.UNAUTHORIZED
                                    )

                            val req = try {
                                val bodyString = RequestBodyExtractor.extractBodyAsString(exchange)
                                mapper.readValue(bodyString, ApiRequestImportPlaylist::class.java)
                                    .takeIf { it.spotifyPlaylistId.isNotBlank() }
                                    ?: throw IllegalArgumentException("spotifyPlaylistId is blank")
                            } catch (e: Exception) {
                                when (e) {
                                    is IllegalArgumentException,
                                    is ValueInstantiationException,
                                    is MismatchedInputException,
                                    is JsonParseException,
                                        -> {
                                        logger.info("Invalid import playlist request: {}", e.message)
                                        return@mono invalidRequestResponse()
                                    }

                                    else -> throw e
                                }
                            }

                            val request = RequestImportPlaylist.newBuilder().apply {
                                this.spotifyPlaylistId = req.spotifyPlaylistId.trim()
                                this.user = RequestUser.newBuilder().apply {
                                    email = user.email
                                    type = when (user.userType) {
                                        UserType.EMAIL -> RequestUser.UserType.EMAIL
                                        UserType.GOOGLE -> RequestUser.UserType.GOOGLE
                                    }
                                }.build()
                            }.build()

                            val response = playlist.importPlaylist(request).await()

                            val jsonString = JsonFormat.printer()
                                .includingDefaultValueFields()
                                .print(response)

                            ResponseEntity(jsonString, HttpStatus.OK)
                        }.wrapResponse(exchange, mapper).onErrorResume { error ->
                            val (httpStatus, responseStatus) = error.toResponseStatus()

                            if (httpStatus.is5xxServerError) logger.error("Error in PlaylistInterceptor", error)
                            else logger.info("Import playlist rejected: {}", error.message)

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

    private fun invalidRequestResponse() = ResponseEntity(
        ResponseWrapper(
            status = CustomResponseStatus.INVALID_REQUEST_BODY,
            payload = CustomResponseStatus.INVALID_REQUEST_BODY.message
        ),
        HttpStatus.BAD_REQUEST
    )

    private fun Throwable.toResponseStatus(): Pair<HttpStatus, CustomResponseStatus> {
        if (this is NonRetryableAuthenticationException) return HttpStatus.UNAUTHORIZED to CustomResponseStatus.UNAUTHORIZED

        val code = (this as? StatusRuntimeException)?.status?.code
            ?: (cause as? StatusRuntimeException)?.status?.code

        return when (code) {
            Status.Code.INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST to CustomResponseStatus.INVALID_REQUEST_BODY
            Status.Code.NOT_FOUND,
            Status.Code.FAILED_PRECONDITION,
                -> HttpStatus.NOT_FOUND to CustomResponseStatus.NOT_FOUND

            Status.Code.RESOURCE_EXHAUSTED -> HttpStatus.TOO_MANY_REQUESTS to CustomResponseStatus.RATE_LIMITED
            Status.Code.UNAVAILABLE,
            Status.Code.DEADLINE_EXCEEDED,
                -> HttpStatus.SERVICE_UNAVAILABLE to CustomResponseStatus.SERVICE_UNAVAILABLE

            else -> HttpStatus.INTERNAL_SERVER_ERROR to CustomResponseStatus.INTERNAL_SERVER_ERROR
        }
    }
}
