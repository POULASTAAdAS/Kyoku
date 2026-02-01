package com.poulastaa.kyoku.gateway.interceptors.setup

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.google.protobuf.util.JsonFormat
import com.poulastaa.kyoku.gateway.interceptors.ValidationFilter
import com.poulastaa.kyoku.gateway.interceptors.generalErrorResponse
import com.poulastaa.kyoku.gateway.interceptors.wrapResponse
import com.poulastaa.kyoku.gateway.model.ServiceConfigPayload
import com.poulastaa.kyoku.gateway.model.request.ApiRequestSaveGenre
import com.poulastaa.kyoku.gateway.model.response.CustomResponseStatus
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
import com.poulastaa.kyoku.gateway.utils.RequestBodyExtractor
import com.poulastaa.kyoku.grpc.activity_save_genre.SaveGenreGrpc
import com.poulastaa.kyoku.grpc.gateway_setup.GatewaySetupServiceGrpc
import com.poulastaa.kyoku.grpc.model.EmptyResponse
import com.poulastaa.kyoku.grpc.model.GenreRequest
import com.poulastaa.kyoku.grpc.model.RequestSaveUserGenre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.reactor.mono
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

@Configuration
class GenreRouteConfig {
    @GrpcClient("content")
    private lateinit var getGeneStub: GatewaySetupServiceGrpc.GatewaySetupServiceFutureStub
    private val getGenre: GatewaySetupServiceGrpc.GatewaySetupServiceFutureStub
        get() = getGeneStub.withDeadlineAfter(5, TimeUnit.SECONDS)

    @GrpcClient("activity")
    private lateinit var setGeneStub: SaveGenreGrpc.SaveGenreFutureStub
    private val setGenre: SaveGenreGrpc.SaveGenreFutureStub
        get() = setGeneStub.withDeadlineAfter(5, TimeUnit.SECONDS)

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

                        val response = getGenre.getGenre(
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
                    }.wrapResponse(exchange, mapper).generalErrorResponse(
                        errorTag = "Error in PlaylistInterceptor",
                        exchange = exchange,
                        mapper = mapper
                    )
                }
            }.uri(service.uri)
        }.build()!!

    @Bean
    fun provideSaveGenreRoute(
        builder: RouteLocatorBuilder,
        @Qualifier("provideActivityServicePayload") service: ServiceConfigPayload,
        validationFilter: ValidationFilter,
        mapper: ObjectMapper,
    ) = builder.routes()
        .route(service.id) { r ->
            r.path("${service.path}save_genre")
                .and()
                .method(HttpMethod.POST)
                .filters { f ->
                    f.filter(validationFilter)

                    f.filter { exchange, _ ->
                        mono(Dispatchers.IO) {
                            // Extract request body using Spring-recommended approach
                            val req = try {
                                val bodyString = RequestBodyExtractor.extractBodyAsString(exchange)
                                val req = mapper.readValue(bodyString, ApiRequestSaveGenre::class.java)
                                if (req.userId <= 0) throw IllegalArgumentException()

                                req
                            } catch (e: Exception) {
                                when (e) {
                                    is IllegalArgumentException, is ValueInstantiationException, is JsonParseException -> {
                                        println("Invalid request in SaveGenreInterceptor: ${e.message}")
                                        return@mono ResponseEntity(
                                            ResponseWrapper(
                                                status = CustomResponseStatus.INVALID_REQUEST_BODY,
                                                payload = CustomResponseStatus.INVALID_REQUEST_BODY.message
                                            ),
                                            HttpStatus.BAD_REQUEST
                                        )
                                    }

                                    else -> throw e // Let generalErrorResponse handle it
                                }
                            }

                            setGenre.storeUserGenre(
                                RequestSaveUserGenre.newBuilder().apply {
                                    this.userId = req.userId
                                    addAllGenreList(req.genreIdList)
                                }.build()
                            ).await()

                            ResponseEntity(
                                ResponseWrapper<EmptyResponse>(status = CustomResponseStatus.SUCCESS),
                                HttpStatus.OK
                            )
                        }.flatMap { responseEntity ->
                            val response = exchange.response
                            response.statusCode = responseEntity.statusCode
                            response.headers.contentType = MediaType.APPLICATION_JSON

                            val bytes = when (val body = responseEntity.body) {
                                is ResponseWrapper<*> -> mapper.writeValueAsBytes(body) // It's an error object
                                else -> ByteArray(0)
                            }
                            response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)))
                        }.onErrorResume { e -> // handle empty request body error
                            when (e) {
                                is IllegalArgumentException, is ValueInstantiationException -> {
                                    println("Invalid request in SaveGenreInterceptor: ${e.message}")
                                    e.printStackTrace()

                                    val response = exchange.response
                                    response.headers.contentType = MediaType.APPLICATION_JSON
                                    response.statusCode = HttpStatus.BAD_REQUEST
                                    val errorWrapper = ResponseEntity(
                                        ResponseWrapper(
                                            status = CustomResponseStatus.INVALID_REQUEST_BODY,
                                            payload = CustomResponseStatus.INVALID_REQUEST_BODY.message
                                        ),
                                        HttpStatus.BAD_REQUEST
                                    )

                                    val bytes = mapper.writeValueAsBytes(errorWrapper)
                                    response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)))
                                }

                                else -> throw e
                            }
                        }.generalErrorResponse(
                            errorTag = "Error in SaveGenreInterceptor",
                            exchange = exchange,
                            mapper = mapper
                        )
                    }
                }.uri(service.uri)
        }.build()!!
}
