package com.poulastaa.auth.domain.model

enum class UsernameState {
    VALID,
    INVALID,
    INVALID_START_WITH_UNDERSCORE,
    EMPTY,
    TOO_LONG
}