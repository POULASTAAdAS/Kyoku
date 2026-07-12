package com.poulastaa.common.network.model

import kotlinx.serialization.Serializable

/**
 * Error body shape returned by backend services.
 *
 * [status] should match either an endpoint-specific [com.poulastaa.common.network.ApiError] enum
 * entry or a shared [com.poulastaa.common.network.ApiError.Network] entry. The network layer maps
 * it to the strongest typed error it can, then falls back to shared network errors.
 */
@Serializable
data class ApiErrorResponse(
    val status: String,
    val message: String,
    val code: Int,
)
