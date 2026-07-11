package com.poulastaa.common.network

interface Error

interface ApiError : Error {
    enum class Network : ApiError {
        NO_INTERNET,
        SERVER_ERROR,
        NOT_FOUND,
        SERIALISATION,
        SOMETHING_WENT_WRONG,
    }

    enum class Authentication : ApiError {
        UNAUTHORIZED,
        PASSWORD_DOES_NOT_MATCH,
        EMAIL_ALREADY_EXISTS,
        EMAIL_NOT_FOUND,
        INVALID_EMAIL,
        INVALID_PASSWORD,
        EMAIL_NOT_VERIFIED,
        ACCOUNT_NOT_FOUND,
    }
}

fun ApiError.toErrorResponse(
    message: String? = null,
    code: Int = -1,
) = ErrorResponse(this, code, message ?: "")