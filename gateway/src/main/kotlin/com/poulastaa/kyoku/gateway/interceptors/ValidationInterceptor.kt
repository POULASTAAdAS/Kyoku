package com.poulastaa.kyoku.gateway.interceptors

import com.fasterxml.jackson.databind.ObjectMapper
import com.poulastaa.kyoku.gateway.model.UserType
import com.poulastaa.kyoku.gateway.model.dto.DtoAuthenticationTokenClaim
import com.poulastaa.kyoku.gateway.model.request.EmptyRequest
import com.poulastaa.kyoku.gateway.model.response.ResponseStatus
import com.poulastaa.kyoku.gateway.model.response.ResponseWrapper
import com.poulastaa.kyoku.gateway.utils.NonRetryableAuthenticationException
import com.poulastaa.kyoku.gateway.utils.RetryableAuthenticationException
import com.poulastaa.kyoku.grpc.validation.ValidationRequest
import com.poulastaa.kyoku.grpc.validation.ValidationServiceGrpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.reactor.mono
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration
import java.util.concurrent.TimeUnit
import com.poulastaa.kyoku.grpc.validation.ResponseStatus as GrpcResponseStatus
import com.poulastaa.kyoku.grpc.validation.UserType as GrpcUserType

@Component // Changed from @Configuration to @Component so it can be injected
class ValidationFilter(
    private val mapper: ObjectMapper,
) : GatewayFilter { // Must implement GatewayFilter, not WebFilter

    @GrpcClient("validator")
    private lateinit var validatorStub: ValidationServiceGrpc.ValidationServiceFutureStub

    private val validator: ValidationServiceGrpc.ValidationServiceFutureStub
        get() = validatorStub.withDeadlineAfter(5, TimeUnit.SECONDS)

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val token = exchange.request.headers
            .getFirst(HttpHeaders.AUTHORIZATION)?.removePrefix(BEARER_PREFIX)
            ?: return writeErrorResponse(
                exchange,
                HttpStatus.UNAUTHORIZED,
                ResponseWrapper(ResponseStatus.UNAUTHORIZED, "Authorization header missing")
            )

        return mono(Dispatchers.IO) { // Use IO Context for gRPC work
            // Suspend function (non-blocking)
            val response = validator.validateAccessToken(
                ValidationRequest.newBuilder().setToken(token).build()
            ).await()

            when (response.status) {
                GrpcResponseStatus.SUCCESS -> response.payload
                GrpcResponseStatus.TOKEN_EXPIRED -> throw RetryableAuthenticationException(
                    "Token expired",
                    HttpStatus.PRECONDITION_FAILED
                )

                else -> throw NonRetryableAuthenticationException(
                    "Invalid Token",
                    HttpStatus.UNAUTHORIZED
                )
            }
        }.flatMap { user ->
            // Store user in attributes
            exchange.attributes[AUTHENTICATED_USER_KEY] = DtoAuthenticationTokenClaim(
                email = user.email,
                userType = when (user.type) {
                    GrpcUserType.EMAIL -> UserType.EMAIL
                    GrpcUserType.GOOGLE -> UserType.GOOGLE
                    else -> return@flatMap Mono.error(
                        NonRetryableAuthenticationException(
                            "Invalid User Type",
                            HttpStatus.PRECONDITION_FAILED
                        )
                    )
                }
            )
            // Continue the chain
            chain.filter(exchange)
        }.retryWhen(
            Retry.fixedDelay(RETRY_ATTEMPTS, RETRY_DELAY)
                .filter { it is RetryableAuthenticationException }
        ).onErrorResume { error ->
            when (error) {
                is NonRetryableAuthenticationException -> {
                    writeErrorResponse(
                        exchange,
                        error.status,
                        ResponseWrapper<EmptyRequest>(status = error.responseStatus)
                    )
                }

                is RetryableAuthenticationException -> {
                    writeErrorResponse(
                        exchange,
                        error.status,
                        ResponseWrapper<EmptyRequest>(status = error.responseStatus)
                    )
                }

                else -> {
                    // Log unexpected errors
                    println("Unexpected error in ValidationFilter: ${error.message}")
                    error.printStackTrace()
                    writeErrorResponse(
                        exchange,
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ResponseWrapper<EmptyRequest>(status = ResponseStatus.INTERNAL_SERVER_ERROR)
                    )
                }
            }
        }
    }

    private fun writeErrorResponse(
        exchange: ServerWebExchange,
        statusCode: HttpStatusCode,
        responseWrapper: ResponseWrapper<*>,
    ): Mono<Void> {
        exchange.response.statusCode = statusCode
        exchange.response.headers.contentType = MediaType.APPLICATION_JSON
        val bytes = mapper.writeValueAsBytes(responseWrapper)
        return exchange.response.writeWith(
            Mono.just(exchange.response.bufferFactory().wrap(bytes))
        )
    }

    companion object {
        const val AUTHENTICATED_USER_KEY = "authenticated-user"
        private const val BEARER_PREFIX = "Bearer "
        private const val RETRY_ATTEMPTS = 3L
        private val RETRY_DELAY = Duration.ofSeconds(1)
    }
}