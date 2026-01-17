package com.poulastaa.kyoku.gateway.utils

import com.poulastaa.kyoku.gateway.model.response.CustomResponseStatus
import org.springframework.http.HttpStatus

class NonRetryableAuthenticationException(
    message: String,
    val status: HttpStatus,
    val responseStatus: CustomResponseStatus = CustomResponseStatus.UNAUTHORIZED,
) : RuntimeException(message)

class RetryableAuthenticationException(
    message: String,
    val status: HttpStatus,
    val responseStatus: CustomResponseStatus = CustomResponseStatus.INTERNAL_SERVER_ERROR,
) : RuntimeException(message)