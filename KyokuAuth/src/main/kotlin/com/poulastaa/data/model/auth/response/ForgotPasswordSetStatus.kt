package com.poulastaa.data.model.auth.response

import kotlinx.serialization.Serializable

@Serializable
enum class ForgotPasswordSetStatus {
    SENT,
    NO_USER_FOUND
}