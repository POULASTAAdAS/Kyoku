package com.poulastaa.kyoku.gateway.interceptors

import com.fasterxml.jackson.databind.ObjectMapper
import com.poulastaa.kyoku.gateway.model.response.CustomResponseStatus
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

fun GatewayFilterSpec.wrapResponseWrapperBody() = this.modifyResponseBody(
    ResponseWrapper::class.java,
    Any::class.java,
) { exchange, response ->
    response?.let {
        Mono.just(it.toResponseEnvelope(exchange.response.statusCode) { statusCode ->
            exchange.response.statusCode = statusCode
        })
    } ?: Mono.empty()
}!!

fun Mono<out ResponseEntity<*>>.wrapResponse(
    exchange: ServerWebExchange,
    mapper: ObjectMapper,
): Mono<Void> = this.flatMap { responseEntity ->
    val response = exchange.response
    response.statusCode = responseEntity.statusCode
    response.headers.contentType = MediaType.APPLICATION_JSON

    val body = responseEntity.body.toResponseEnvelope(
        responseCode = responseEntity.statusCode,
        mapper = mapper,
    ) { statusCode ->
        response.statusCode = statusCode
    }

    val bytes = mapper.writeValueAsBytes(body)
    response.writeBytes(bytes)
}

fun ServerWebExchange.writeResponseWrapper(
    statusCode: HttpStatusCode,
    responseWrapper: ResponseWrapper<*>,
    mapper: ObjectMapper,
): Mono<Void> {
    val response = this.response
    response.statusCode = statusCode
    response.headers.contentType = MediaType.APPLICATION_JSON

    val body = responseWrapper.toResponseEnvelope(statusCode) { resolvedStatusCode ->
        response.statusCode = resolvedStatusCode
    }

    val bytes = mapper.writeValueAsBytes(body)
    return response.writeBytes(bytes)
}

fun Mono<Void>.generalErrorResponse(
    errorTag: String,
    exchange: ServerWebExchange,
    status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    mapper: ObjectMapper,
): Mono<Void> = this.onErrorResume { error ->
    println("$errorTag: ${error.message}")
    error.printStackTrace()

    exchange.writeResponseWrapper(
        statusCode = status,
        responseWrapper = ResponseWrapper(
            status = CustomResponseStatus.INTERNAL_SERVER_ERROR,
            payload = CustomResponseStatus.INTERNAL_SERVER_ERROR.message
        ),
        mapper = mapper
    )
}

private fun Any?.toResponseEnvelope(
    responseCode: HttpStatusCode?,
    mapper: ObjectMapper,
    setStatusCode: (HttpStatusCode) -> Unit,
): ResponseWrapper<*> = when (this) {
    is ResponseWrapper<*> -> toResponseEnvelope(responseCode, setStatusCode)
    is String -> ResponseWrapper(
        status = responseCode.toResponseStatus(),
        payload = toJsonPayload(mapper),
    ).toResponseEnvelope(responseCode, setStatusCode)

    else -> ResponseWrapper(
        status = responseCode.toResponseStatus(),
        payload = this,
    ).toResponseEnvelope(responseCode, setStatusCode)
}

private fun ResponseWrapper<*>.toResponseEnvelope(
    responseCode: HttpStatusCode?,
    setStatusCode: (HttpStatusCode) -> Unit,
): ResponseWrapper<*> {
    val resolvedStatusCode = status.toHttpStatus(responseCode)
    if (responseCode?.value() != resolvedStatusCode.value()) setStatusCode(resolvedStatusCode)

    return ResponseWrapper(
        status = status,
        payload = payload,
        message = toResponseMessage(),
        code = resolvedStatusCode.value(),
    )
}

private fun CustomResponseStatus.isSuccessStatus() = when (this) {
    CustomResponseStatus.USER_CREATED,
    CustomResponseStatus.USER_FOUND,
    CustomResponseStatus.USER_FOUND_NO_PLAYLIST,
    CustomResponseStatus.USER_FOUND_NO_ARTIST,
    CustomResponseStatus.USER_FOUND_NO_GENRE,
    CustomResponseStatus.USER_FOUND_NO_B_DATE,
    CustomResponseStatus.SUCCESS,
        -> true

    else -> false
}

private fun CustomResponseStatus.toSuccessHttpStatus() = when (this) {
    CustomResponseStatus.USER_CREATED -> HttpStatus.CREATED
    else -> HttpStatus.OK
}

private fun CustomResponseStatus.toHttpStatus(
    responseCode: HttpStatusCode?,
) = if (isSuccessStatus()) {
    responseCode.takeIf { it.isIn(200..299) } ?: toSuccessHttpStatus()
} else toErrorHttpStatus(responseCode)

private fun CustomResponseStatus.toErrorHttpStatus(
    responseCode: HttpStatusCode?,
): HttpStatusCode = responseCode.takeIf { it.isIn(400..599) } ?: when (this) {
    CustomResponseStatus.EMAIL_ALREADY_IN_USE -> HttpStatus.CONFLICT
    CustomResponseStatus.EMAIL_NOT_VALID -> HttpStatus.FORBIDDEN
    CustomResponseStatus.PASSWORD_DOES_NOT_MATCH -> HttpStatus.FORBIDDEN
    CustomResponseStatus.INVALID_PASSWORD -> HttpStatus.FORBIDDEN
    CustomResponseStatus.USER_NOT_FOUND,
    CustomResponseStatus.NOT_FOUND,
    CustomResponseStatus.NO_CONTENT,
        -> HttpStatus.NOT_FOUND

    CustomResponseStatus.UNAUTHORIZED -> HttpStatus.UNAUTHORIZED
    CustomResponseStatus.SERVICE_UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE
    CustomResponseStatus.INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
    CustomResponseStatus.METHOD_NOT_ALLOWED -> HttpStatus.METHOD_NOT_ALLOWED
    CustomResponseStatus.INVALID_REQUEST_BODY -> HttpStatus.BAD_REQUEST
    else -> HttpStatus.BAD_REQUEST
}

private fun HttpStatusCode?.toResponseStatus() = when (this?.value()) {
    in 200..299 -> CustomResponseStatus.SUCCESS
    400 -> CustomResponseStatus.INVALID_REQUEST_BODY
    401 -> CustomResponseStatus.UNAUTHORIZED
    404 -> CustomResponseStatus.NOT_FOUND
    405 -> CustomResponseStatus.METHOD_NOT_ALLOWED
    503 -> CustomResponseStatus.SERVICE_UNAVAILABLE
    in 500..599 -> CustomResponseStatus.INTERNAL_SERVER_ERROR
    else -> CustomResponseStatus.INTERNAL_SERVER_ERROR
}

private fun ResponseWrapper<*>.toResponseMessage(): String? {
    message?.takeIf { it.isNotBlank() }?.let { return it }
    if (status.isSuccessStatus()) return null

    return (payload as? String)?.takeIf { it.isNotBlank() } ?: when (status) {
        CustomResponseStatus.EMAIL_NOT_VALID -> "Check your email"
        CustomResponseStatus.EMAIL_ALREADY_IN_USE -> "Email already in use"
        CustomResponseStatus.PASSWORD_DOES_NOT_MATCH -> "Wrong password"
        CustomResponseStatus.INVALID_PASSWORD -> CustomResponseStatus.INVALID_PASSWORD.message
        CustomResponseStatus.USER_NOT_FOUND -> "No account found"
        CustomResponseStatus.NOT_FOUND -> CustomResponseStatus.NOT_FOUND.message
        CustomResponseStatus.NO_CONTENT -> "Couldn't find that"
        CustomResponseStatus.SERVICE_UNAVAILABLE -> CustomResponseStatus.SERVICE_UNAVAILABLE.message
        CustomResponseStatus.INTERNAL_SERVER_ERROR -> CustomResponseStatus.INTERNAL_SERVER_ERROR.message
        CustomResponseStatus.UNAUTHORIZED -> CustomResponseStatus.UNAUTHORIZED.message
        CustomResponseStatus.INVALID_REQUEST_BODY -> CustomResponseStatus.INVALID_REQUEST_BODY.message
        CustomResponseStatus.METHOD_NOT_ALLOWED -> CustomResponseStatus.METHOD_NOT_ALLOWED.message
        else -> "Something went wrong"
    } ?: "Something went wrong"
}

private fun String.toJsonPayload(mapper: ObjectMapper): Any? {
    if (isBlank()) return null

    return runCatching { mapper.readTree(this) as Any }.getOrElse { this }
}

private fun HttpStatusCode?.isIn(range: IntRange) = this?.value()?.let { it in range } == true

private fun ServerHttpResponse.writeBytes(bytes: ByteArray): Mono<Void> =
    writeWith(Mono.just(bufferFactory().wrap(bytes)))
