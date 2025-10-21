package com.poulastaa.auth.network.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
enum class UpdatePasswordStatus {
    UPDATED,
    USER_NOT_FOUND,
    SAME_PASSWORD,
    INVALID_PASSWORD,
    EXPIRED_TOKEN,
    ERROR,
    INVALID_TOKEN
}