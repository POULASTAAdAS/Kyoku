package com.poulastaa.auth.domain.model

enum class PasswordStatus {
    VALID,
    EMPTY,
    TOO_SHORT,
    TOO_LONG,
    INVALID
}