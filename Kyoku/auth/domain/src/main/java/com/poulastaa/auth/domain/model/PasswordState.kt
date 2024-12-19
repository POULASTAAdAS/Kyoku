package com.poulastaa.auth.domain.model

enum class PasswordState {
    VALID,
    EMPTY,
    TOO_SHORT,
    TOO_LONG,
    INVALID
}