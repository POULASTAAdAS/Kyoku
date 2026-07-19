package com.poulastaa.kyoku.auth.model.response

import org.springframework.http.HttpStatus

enum class ResponseStatus(
    val code: HttpStatus,
    val message: String? = null,
) {
    SUCCESS(HttpStatus.OK),
    USER_CREATED(HttpStatus.CREATED),
    USER_FOUND(HttpStatus.OK),
    USER_FOUND_NO_PLAYLIST(HttpStatus.OK),
    USER_FOUND_NO_ARTIST(HttpStatus.OK),
    USER_FOUND_NO_GENRE(HttpStatus.OK),
    USER_FOUND_NO_B_DATE(HttpStatus.OK),

    EMAIL_NOT_VALID(HttpStatus.FORBIDDEN, "Check your email"),
    PASSWORD_DOES_NOT_MATCH(HttpStatus.FORBIDDEN, "Wrong password"),
    INVALID_PASSWORD(HttpStatus.FORBIDDEN, "Check your password"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "No account found"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Couldn't find that"),

    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "Request body is not valid"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "You do not have permission to access this resource"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable, please try again later"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Opps! Something went wrong. Please try again later"),
    EMAIL_ALREADY_IN_USE(HttpStatus.CONFLICT, "Email already in use"),
}
