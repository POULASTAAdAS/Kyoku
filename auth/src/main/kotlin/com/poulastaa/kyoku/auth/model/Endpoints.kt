package com.poulastaa.kyoku.auth.model

object Endpoints {
    private const val VERSION: String = "v1"
    private const val API = "api"
    private const val TYPE = "auth"

    private const val STATIC = "$API/$VERSION/$TYPE"

    const val EMAIL_SING_IN = "$STATIC/email/login"
    const val EMAIL_SING_UP = "$STATIC/email/create-account"
    const val VERIFY_EMAIL = "$STATIC/email/verify-email"
    const val CHECK_VERIFICATION_MAIL_STATE = "$STATIC/email/verify-email/state"
    const val REFRESH_TOKEN = "$STATIC/email/refresh-token"

    const val FORGOT_PASSWORD = "$STATIC/forgot-password"
    const val VALIDATE_PASSWORD_OTP = "$STATIC/forgot-password/validate"
    const val RESET_PASSWORD = "$STATIC/reset-password"

    const val GOOGLE_AUTH = "$STATIC/google/join"

    /**
     * CHANGES WILL BREAK THE FLOW :)
     *
     *
     *      - IMPORTANT this route is the file-service static file route!!!
     *      - DON'T CHANGE OR MAKE SURE TO UPDATE IF FILE SERVER ROUTE CHANGES
     */
    const val STATIC_FILE = "$API/$VERSION/file/static"
}