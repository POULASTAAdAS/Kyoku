package com.poulastaa.kyoku.gateway.model.response

data class ResponseWrapper<T>(
    val status: CustomResponseStatus = CustomResponseStatus.UNAUTHORIZED,
    val payload: T? = null,
)
