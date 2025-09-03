package com.poulastaa.kyoku.auth.model.response

data class ResponseWrapper<T>(
    val status: ResponseStatus = ResponseStatus.UNAUTHORIZED,
    val payload: T? = null,
)
