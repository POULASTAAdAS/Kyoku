package com.poulastaa.kyoku.data.model.api.auth

import kotlinx.serialization.Serializable

@Serializable
enum class UserCreationStatus {
    CREATED,
    CONFLICT,
    SOMETHING_WENT_WRONG,
    TOKEN_NOT_VALID,
    USER_NOT_FOUND,
    EMAIL_NOT_VALID
}