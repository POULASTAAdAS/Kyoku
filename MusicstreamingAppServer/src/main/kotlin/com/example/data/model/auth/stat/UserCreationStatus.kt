package com.example.data.model.auth.stat

import kotlinx.serialization.Serializable

@Serializable
enum class UserCreationStatus {
    CREATED,
    CONFLICT,
    SOMETHING_WENT_WRONG,
    EMAIL_NOT_VALID
}