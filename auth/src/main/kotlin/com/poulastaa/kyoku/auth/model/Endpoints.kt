package com.poulastaa.kyoku.auth.model

object Endpoints {
    private const val API = "api"
    private const val VERSION: String = "v1"
    private const val TYPE = "auth"

    private const val STATIC = "$API/$VERSION/$TYPE"

    const val EMAIL_SING_IN = "$STATIC/email/login"
    const val EMAIL_SING_UP = "$STATIC/email/create-account"
    const val VERIFY_EMAIL = "$STATIC/email/verify-email"
    const val CHECK_VERIFICATION_MAIL_STATE = "$STATIC/email/verify-email/state"
    const val REFRESH_TOKEN = "$STATIC/refresh-token"

    const val FORGOT_PASSWORD = "$STATIC/email/forgot-password"
    const val VALIDATE_PASSWORD_OTP = "$STATIC/email/forgot-password/validate"
    const val RESET_PASSWORD = "$STATIC/email/reset-password"

    const val GOOGLE_AUTH = "$STATIC/google/join"
}