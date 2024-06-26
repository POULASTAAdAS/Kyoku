package com.poulastaa.auth.data.model.res

import kotlinx.serialization.Serializable

@Serializable
enum class ForgotPasswordSetStatusDao {
    SENT,
    NO_USER_FOUND
}