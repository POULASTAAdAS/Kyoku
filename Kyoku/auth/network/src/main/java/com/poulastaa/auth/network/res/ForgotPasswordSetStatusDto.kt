package com.poulastaa.auth.network.res

import kotlinx.serialization.Serializable

@Serializable
enum class ForgotPasswordSetStatusDto {
    SENT,
    NO_USER_FOUND
}