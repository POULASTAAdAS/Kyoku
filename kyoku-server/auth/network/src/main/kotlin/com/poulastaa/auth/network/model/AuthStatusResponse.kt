package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class AuthStatusResponse {
    CREATED,
    USER_FOUND,
    USER_FOUND_STORE_B_DATE,
    USER_FOUND_SET_GENRE,
    USER_FOUND_SET_ARTIST,
    USER_FOUND_HOME,
    EMAIL_NOT_VERIFIED,
    EMAIL_ALREADY_IN_USE,
    INVALID_EMAIL,
    PASSWORD_DOES_NOT_MATCH,
    USER_NOT_FOUND,
    TOKEN_EXPIRED,
    SERVER_ERROR
}