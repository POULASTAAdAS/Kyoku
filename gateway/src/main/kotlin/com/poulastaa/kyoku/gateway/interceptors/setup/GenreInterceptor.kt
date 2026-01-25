package com.poulastaa.kyoku.gateway.interceptors.setup

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.protobuf.util.JsonFormat
import com.poulastaa.kyoku.gateway.interceptors.ValidationFilter
import com.poulastaa.kyoku.gateway.interceptors.wrapResponse
import com.poulastaa.kyoku.gateway.model.ServiceConfigPayload
import com.poulastaa.kyoku.gateway.model.response.CustomResponseStatus
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
import com.poulastaa.kyoku.grpc.gateway_setup.GatewaySetupServiceGrpc
import com.poulastaa.kyoku.grpc.model.GenreRequest
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
import java.util.concurrent.TimeUnit

@Configuration
class GenreRouteConfig {
    @GrpcClient("content")
    private lateinit var geneStub: GatewaySetupServiceGrpc.GatewaySetupServiceFutureStub
    private val genre: GatewaySetupServiceGrpc.GatewaySetupServiceFutureStub
        get() = geneStub.withDeadlineAfter(5, TimeUnit.SECONDS)

    @Bean
    fun provideSelectGenreRoute(
        builder: RouteLocatorBuilder,
        @Qualifier("provideContentServicePayload") service: ServiceConfigPayload,
        validationFilter: ValidationFilter, // Inject the corrected filter
        mapper: ObjectMapper,
    ) = builder.routes()
        .route(service.id) { r ->
            r.path("${service.path}pick_genre").filters { f ->
                f.filter(validationFilter)

                f.filter { exchange, _ ->
                    mono(Dispatchers.IO) {
                        val page = exchange.request.queryParams["page"]?.firstOrNull()?.toIntOrNull()
                            ?: return@mono ResponseEntity(
                                ResponseWrapper(
                                    status = CustomResponseStatus.UNAUTHORIZED,
                                    payload = "Missing page"
                                ),
                                HttpStatus.BAD_REQUEST
                            )

                        val size = exchange.request.queryParams["size"]?.firstOrNull()?.toIntOrNull()
                            ?: return@mono ResponseEntity(
                                ResponseWrapper(
                                    status = CustomResponseStatus.UNAUTHORIZED,
                                    payload = "Missing size"
                                ),
                                HttpStatus.BAD_REQUEST
                            )

                        val query = exchange.request.queryParams["query"]?.firstOrNull()

                        val response = genre.getGenre(
                            GenreRequest.newBuilder().apply {
                                this.page = page
                                this.size = size
                                query?.let { this.query = it }
                            }.build()
                        ).await()

                        val jsonString = JsonFormat.printer()
                            .includingDefaultValueFields()
                            .print(response)

                        ResponseEntity(jsonString, HttpStatus.OK)
                    }.wrapResponse(exchange, mapper).onErrorResume { error ->
                        println("Error in PlaylistInterceptor: ${error.message}")
                        error.printStackTrace()

                        val response = exchange.response
                        response.headers.contentType = MediaType.APPLICATION_JSON

                        response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
                        val errorWrapper = ResponseWrapper(
                            status = CustomResponseStatus.INTERNAL_SERVER_ERROR,
                            payload = CustomResponseStatus.INTERNAL_SERVER_ERROR.message
                        )

                        val bytes = mapper.writeValueAsBytes(errorWrapper)
                        response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)))
                    }
                }
            }.uri(service.uri)
        }.build()!!
}
