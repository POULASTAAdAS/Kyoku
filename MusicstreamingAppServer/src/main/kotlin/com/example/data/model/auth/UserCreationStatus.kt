package com.example.data.model.auth

enum class UserCreationStatus {
    CREATED,
    CONFLICT,
    SOMETHING_WENT_WRONG,
    EMAIL_NOT_VALID
}