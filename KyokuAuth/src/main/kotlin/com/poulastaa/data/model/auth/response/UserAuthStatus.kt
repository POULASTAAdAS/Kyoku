package com.poulastaa.data.model.auth.response

import kotlinx.serialization.Serializable

@Serializable
enum class UserAuthStatus {
    CREATED,
    CONFLICT,
    USER_FOUND_HOME,
    USER_FOUND_STORE_B_DATE,
    USER_FOUND_SET_GENRE,
    USER_FOUND_SET_ARTIST,
    PASSWORD_DOES_NOT_MATCH,
    TOKEN_NOT_VALID,
    USER_NOT_FOUND,
    EMAIL_NOT_VALID,
    EMAIL_NOT_VERIFIED,
    SOMETHING_WENT_WRONG
}