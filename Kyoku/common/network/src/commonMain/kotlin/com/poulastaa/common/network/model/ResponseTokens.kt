package com.poulastaa.common.network.model

import com.poulastaa.common.domain.model.DtoTokens
import kotlinx.serialization.Serializable

@Serializable
data class ResponseTokens(
    val accessToken: String,
    val refreshToken: String,
) {
    fun toDto() = DtoTokens(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
}
