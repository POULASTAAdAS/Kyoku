package com.poulastaa.kyoku.auth.model.response

import org.springframework.http.HttpStatus

enum class RefreshTokenResponseStatus(val status: HttpStatus) {
    SUCCESS(HttpStatus.OK),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(HttpStatus.NOT_ACCEPTABLE)
}