package com.poulastaa.kyoku.gateway.model.response

enum class CustomResponseStatus(val message: String? = null) {
    USER_CREATED,
    USER_FOUND,
    USER_FOUND_NO_PLAYLIST,
    USER_FOUND_NO_ARTIST,
    USER_FOUND_NO_GENRE,
    USER_FOUND_NO_B_DATE,
    EMAIL_NOT_VALID,
    PASSWORD_DOES_NOT_MATCH,
    EMAIL_ALREADY_IN_USE,

    // retriable error
    SERVICE_UNAVAILABLE("Service unavailable, please try again later"),
    INTERNAL_SERVER_ERROR("Opps! Something went wrong. Please try again later"),

    // non retriable error
    USER_NOT_FOUND("No user found, please register first"),
    NO_CONTENT,
    UNAUTHORIZED("You do not have permission to access this resource"),
    FUCK_YOU("FUCK YOU"),
}