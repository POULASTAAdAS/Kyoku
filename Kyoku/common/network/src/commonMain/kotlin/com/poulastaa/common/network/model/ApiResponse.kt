package com.poulastaa.common.network.model

import kotlinx.serialization.Serializable

/**
 * Generic API envelope returned by the gateway.
 *
 * [status] is the backend response status, [payload] carries endpoint-specific data on success,
 * and [message]/[code] provide the shared error metadata used by the network layer.
 */
@Serializable
data class ApiResponse<out PAYLOAD>(
    val status: String,
    val payload: PAYLOAD? = null,
    val message: String? = null,
    val code: Int = -1,
)
