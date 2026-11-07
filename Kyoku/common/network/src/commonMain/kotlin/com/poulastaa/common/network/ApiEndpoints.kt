package com.poulastaa.common.network

data object ApiEndpoints {
    private const val API = "api"
    private const val VERSION = "v1"

    private const val TYPE_AUTH = "auth"
    private const val TYPE_PLAYLIST = "playlist"

    private const val PREFIX_AUTH = "/$API/$VERSION/$TYPE_AUTH"
    private const val PREFIX_PLAYLIST = "/$API/$VERSION/$TYPE_PLAYLIST"

    data object Auth {
        const val SIGN_IN = "$PREFIX_AUTH/email/login"
        const val SIGN_UP = "$PREFIX_AUTH/email/create-account"
        const val GOOGLE_AUTH = "$PREFIX_AUTH/google/join"
        const val CHECK_VERIFICATION_MAIL_STATE = "$PREFIX_AUTH/email/verify-email/state"

        const val FORGOT_PASSWORD = "$PREFIX_AUTH/email/forgot-password"
        const val VALIDATE_PASSWORD_OTP = "$PREFIX_AUTH/email/forgot-password/validate"
        const val RESET_PASSWORD = "$PREFIX_AUTH/email/reset-password"
    }

    data object Setup {
        const val IMPORT_PLAYLIST = "$PREFIX_PLAYLIST/import_playlist"
    }
}
