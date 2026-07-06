package com.poulastaa.common.network

interface Error

interface ApiError : Error {
    enum class Authentication : ApiError {
        UNAUTHORIZED,
        PASSWORD_DOES_NOT_MATCH,
        EMAIL_ALREADY_EXISTS,
        EMAIL_NOT_FOUND,
        INVALID_EMAIL,
        INVALID_PASSWORD,
        EMAIL_NOT_VERIFIED,
        ACCOUNT_NOT_FOUND,
        SOMETHING_WENT_WRONG,
    }
}