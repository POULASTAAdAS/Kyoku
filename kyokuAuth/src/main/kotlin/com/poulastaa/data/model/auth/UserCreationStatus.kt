package com.poulastaa.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
enum class UserCreationStatus {
    CREATED,
    CONFLICT,
    SOMETHING_WENT_WRONG,
    USER_NOT_FOUND,
    EMAIL_NOT_VALID
}