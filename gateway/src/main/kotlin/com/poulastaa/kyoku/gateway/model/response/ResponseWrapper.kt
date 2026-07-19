package com.poulastaa.kyoku.gateway.model.response

data class ResponseWrapper<T>(
    val status: CustomResponseStatus = CustomResponseStatus.UNAUTHORIZED,
    val payload: T? = null,
    val message: String? = null,
    val code: Int = -1,
)
