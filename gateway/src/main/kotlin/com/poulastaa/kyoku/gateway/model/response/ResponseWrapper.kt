package com.poulastaa.kyoku.gateway.model.response

data class ResponseWrapper<T>(
    val status: ResponseStatus = ResponseStatus.UNAUTHORIZED,
    val payload: T? = null,
)
