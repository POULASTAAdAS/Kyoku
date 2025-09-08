package com.poulastaa.kyoku.auth.model.dto

import org.springframework.http.HttpStatus

enum class DtoEmailVerificationStatus(val status: HttpStatus) {
    VALID(HttpStatus.OK),
    TOKEN_ALREADY_USED(HttpStatus.IM_USED),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR)
}