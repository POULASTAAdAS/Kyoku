package com.poulastaa.kyoku.auth.model

object Endpoints {
    private const val VERSION: String = "v1"
    private const val API = "api"
    private const val TYPE = "auth"

    private const val STATIC = "$API/$VERSION/$TYPE"

    const val EMAIL_SING_IN = "$STATIC/email/login"
    const val EMAIL_SING_UP = "$STATIC/email/create-account"
    const val GET_VERIFY_EMAIL = "$STATIC/email/get-verify-email"
    const val CHECK_VERIFICATION_MAIL_STATE = "$STATIC/email/verify-email/state"

    // TODO --> think how to implement forgot password on device

    const val REFRESH_TOKEN = "$STATIC/email/refresh-token"

    const val GOOGLE_SIGN_UP = "$STATIC/google/join"
}