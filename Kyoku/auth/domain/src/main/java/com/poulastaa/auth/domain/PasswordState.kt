package com.poulastaa.auth.domain

enum class PasswordState {
    VALID,
    EMPTY,
    TOO_SHORT,
    TOO_LONG,
    INVALID
}