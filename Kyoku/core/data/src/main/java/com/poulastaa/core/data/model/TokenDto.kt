package com.poulastaa.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val status: TokenStatusDto =TokenStatusDto.FAILURE,
    val accessToken: String = "",
    val refreshToken: String = "",
)
