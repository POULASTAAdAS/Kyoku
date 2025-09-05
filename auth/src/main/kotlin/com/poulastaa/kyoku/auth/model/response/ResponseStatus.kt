package com.poulastaa.kyoku.auth.model.response

import org.springframework.http.HttpStatus

enum class ResponseStatus(val code: HttpStatus) {
    USER_CREATED(HttpStatus.CREATED),
    USER_FOUND(HttpStatus.FOUND),
    USER_FOUND_NO_PLAYLIST(HttpStatus.FOUND),
    USER_FOUND_NO_ARTIST(HttpStatus.FOUND),
    USER_FOUND_NO_GENRE(HttpStatus.FOUND),
    USER_FOUND_NO_B_DATE(HttpStatus.FOUND),

    EMAIL_NOT_VALID(HttpStatus.FORBIDDEN),
    PASSWORD_DOES_NOT_MATCH(HttpStatus.FORBIDDEN),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_ALREADY_IN_USE(HttpStatus.CONFLICT),
}