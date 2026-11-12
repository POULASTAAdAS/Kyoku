package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

/**
 * Success payload of the reset-password route. The server only returns 2xx with `UPDATED`;
 * every failure arrives as an error envelope and is mapped to [com.poulastaa.common.domain.result.ApiError].
 */
@Serializable
data class ResetPasswordResponse(
    val status: String = "",
)
