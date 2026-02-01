package com.poulastaa.kyoku.gateway.interceptors

import com.fasterxml.jackson.databind.ObjectMapper
import com.poulastaa.kyoku.gateway.model.response.CustomResponseStatus
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

fun Mono<ResponseEntity<out Any>>.wrapResponse(
    exchange: ServerWebExchange,
    mapper: ObjectMapper,
): Mono<Void> = this.flatMap { responseEntity ->
    // Write the ResponseEntity to the ServerWebExchange
    val response = exchange.response
    response.statusCode = responseEntity.statusCode
    response.headers.contentType = MediaType.APPLICATION_JSON

    val bytes = when (val body = responseEntity.body) {
        is String -> body.toByteArray() // It's the JSON string from Proto
        is ResponseWrapper<*> -> mapper.writeValueAsBytes(body) // It's an error object
        else -> ByteArray(0)
    }

    response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)))
}

fun Mono<Void>.generalErrorResponse(
    errorTag: String,
    exchange: ServerWebExchange,
    status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    mapper: ObjectMapper,
): Mono<Void> = this.onErrorResume { error ->
    println("$errorTag: ${error.message}")
    error.printStackTrace()

    val response = exchange.response
    response.headers.contentType = MediaType.APPLICATION_JSON
    response.statusCode = status
    val errorWrapper = ResponseWrapper(
        status = CustomResponseStatus.INTERNAL_SERVER_ERROR,
        payload = CustomResponseStatus.INTERNAL_SERVER_ERROR.message
    )

    val bytes = mapper.writeValueAsBytes(errorWrapper)
    response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)))
}
