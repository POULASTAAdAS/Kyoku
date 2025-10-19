package com.poulastaa.auth.network.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
enum class ResponseAuthResponseStatus {
    USER_CREATED,
    USER_FOUND,
    USER_FOUND_NO_PLAYLIST,
    USER_FOUND_NO_ARTIST,
    USER_FOUND_NO_GENRE,
    USER_FOUND_NO_B_DATE,

    EMAIL_NOT_VALID,
    PASSWORD_DOES_NOT_MATCH,

    USER_NOT_FOUND,

    UNAUTHORIZED,
    INTERNAL_SERVER_ERROR,
    EMAIL_ALREADY_IN_USE,
}
