package com.poulastaa.core.domain

interface DataError : Error {
    enum class Network : DataError {
        UNAUTHORISED,
        EMAIL_NOT_VERIFIED,
        PASSWORD_DOES_NOT_MATCH,
        NOT_FOUND,
        INVALID_EMAIL,
        CONFLICT,
        NO_INTERNET,
        SERVER_ERROR,
        SERIALISATION,
        UNKNOWN
    }

    enum class Local : DataError {
        NAME_CONFLICT,
    }
}