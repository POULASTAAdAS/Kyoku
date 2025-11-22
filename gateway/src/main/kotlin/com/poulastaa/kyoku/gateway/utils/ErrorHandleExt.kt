package com.poulastaa.kyoku.gateway.utils

import com.poulastaa.kyoku.gateway.model.response.ResponseStatus
import org.springframework.http.HttpStatus

class NonRetryableAuthenticationException(
    message: String,
    val status: HttpStatus,
    val responseStatus: ResponseStatus = ResponseStatus.UNAUTHORIZED,
) : RuntimeException(message)

class RetryableAuthenticationException(
    message: String,
    val status: HttpStatus,
    val responseStatus: ResponseStatus = ResponseStatus.INTERNAL_SERVER_ERROR,
) : RuntimeException(message)