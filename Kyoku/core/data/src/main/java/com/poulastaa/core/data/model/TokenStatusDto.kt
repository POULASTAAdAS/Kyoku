package com.poulastaa.core.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class TokenStatusDto {
    SUCCESS,
    FAILURE
}