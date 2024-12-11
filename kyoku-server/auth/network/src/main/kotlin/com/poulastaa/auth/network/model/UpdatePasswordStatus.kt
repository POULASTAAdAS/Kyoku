package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class UpdatePasswordStatus {
    RESET,
    SAME_PASSWORD,
    USER_NOT_FOUND,
    SERVER_ERROR,
    TOKEN_USED,
}