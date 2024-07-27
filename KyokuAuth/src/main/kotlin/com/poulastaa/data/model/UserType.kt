package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserType {
    GOOGLE_USER,
    EMAIL_USER
}