package com.poulastaa.core.domain

interface DataError : Error {
    enum class Network : DataError {
        NO_INTERNET,
        SERVER_ERROR,
        UNKNOWN,
        SERIALISATION,
    }

    enum class Local : DataError {
        NOT_ENOUGH_SPACE,
    }
}