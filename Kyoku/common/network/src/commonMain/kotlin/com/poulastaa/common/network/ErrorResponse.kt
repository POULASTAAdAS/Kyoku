package com.poulastaa.common.network

/**
 * Marker for typed errors used by [ApiResult].
 */
interface Error

/**
 * Typed errors that can come from HTTP/API work.
 *
 * [message] is a short user-facing fallback. Backend responses may still provide a more specific
 * message through [ErrorResponse.message], but UI code can use the enum message when it wants a
 * stable local string.
 */
interface ApiError : Error {
    val message: String

    /**
     * Shared API failures that can happen for any endpoint.
     */
    enum class Network(override val message: String) : ApiError {
        NO_INTERNET("You're offline"),
        SERVER_ERROR("Server unavailable"),
        NOT_FOUND("Couldn't find that"),
        SERIALISATION("Couldn't read response"),
        UNAUTHORIZED("Please sign in again"),
        SOMETHING_WENT_WRONG("Something went wrong"),
    }

    /**
     * Auth endpoint failures returned by the backend as status strings.
     */
    enum class Authentication(override val message: String) : ApiError {
        PASSWORD_DOES_NOT_MATCH("Wrong password"),
        OLD_ACCOUNT_FOUND("Use your existing account"),
        ACCOUNT_NOT_FOUND("No account found"),
        INVALID_EMAIL("Check your email"),
        INVALID_PASSWORD("Check your password"),
        EMAIL_NOT_VERIFIED("Verify your email"),
        EMAIL_ALREADY_IN_USE("Email already in use"),
    }
}

/**
 * Typed errors from local persistence or other non-HTTP data sources.
 */
interface DataError : Error {
    enum class General : DataError {
        NOT_ENOUGH_SPACE,
        SOMETHING_WENT_WRONG,
    }
}

/**
 * Wraps an [ApiError] in the common error response shape.
 *
 * If no explicit [message] is supplied, the enum's user-facing [ApiError.message] is used.
 */
@PublishedApi
internal fun ApiError.toErrorResponse(
    message: String? = null,
    code: Int = -1,
) = ErrorResponse(this, code, message ?: this.message)
