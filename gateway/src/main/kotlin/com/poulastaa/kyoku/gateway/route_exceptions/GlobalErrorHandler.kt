package com.poulastaa.kyoku.gateway.route_exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import com.poulastaa.kyoku.gateway.interceptors.writeResponseWrapper
import com.poulastaa.kyoku.gateway.model.response.CustomResponseStatus
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
@Order(-2)
class GlobalErrorHandler(
    private val mapper: ObjectMapper,
) : ErrorWebExceptionHandler {
    override fun handle(
        exchange: ServerWebExchange,
        ex: Throwable,
    ): Mono<Void> {
        if (exchange.response.isCommitted) return Mono.error(ex)

        val statusCode = ex.toHttpStatusCode()
        val responseStatus = statusCode.toResponseStatus()

        return exchange.writeResponseWrapper(
            statusCode = statusCode,
            responseWrapper = ResponseWrapper(
                status = responseStatus,
                payload = responseStatus.message ?: statusCode.toReasonPhrase(),
            ),
            mapper = mapper
        )
    }

    private fun Throwable.toHttpStatusCode(): HttpStatusCode = when (this) {
        is ResponseStatusException -> statusCode
        else -> when {
            message.containsServiceUnavailableError() -> HttpStatus.SERVICE_UNAVAILABLE
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    private fun HttpStatusCode.toResponseStatus() = when (value()) {
        400 -> CustomResponseStatus.INVALID_REQUEST_BODY
        401 -> CustomResponseStatus.UNAUTHORIZED
        404 -> CustomResponseStatus.NO_CONTENT
        405 -> CustomResponseStatus.METHOD_NOT_ALLOWED
        503 -> CustomResponseStatus.SERVICE_UNAVAILABLE
        in 500..599 -> CustomResponseStatus.INTERNAL_SERVER_ERROR
        else -> CustomResponseStatus.INTERNAL_SERVER_ERROR
    }

    private fun HttpStatusCode.toReasonPhrase() = (this as? HttpStatus)?.reasonPhrase ?: "Something went wrong"

    private fun String?.containsServiceUnavailableError() =
        this?.contains("SERVICE_UNAVAILABLE", ignoreCase = true) == true ||
                this?.contains("Service Unavailable", ignoreCase = true) == true ||
                this?.contains("Unable to find instance", ignoreCase = true) == true
}
