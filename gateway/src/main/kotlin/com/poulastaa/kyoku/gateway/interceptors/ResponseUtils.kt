package com.poulastaa.kyoku.gateway.interceptors

import com.fasterxml.jackson.databind.ObjectMapper
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
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