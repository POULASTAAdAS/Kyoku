package com.poulastaa.common.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val status: String,
    val message: String,
    val code: Int,
)
