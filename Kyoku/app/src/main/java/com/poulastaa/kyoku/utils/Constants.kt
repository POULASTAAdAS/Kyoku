package com.poulastaa.kyoku.utils

object Constants {
    const val AUTH_BASE_URL = "https://7204-103-44-173-12.ngrok-free.app"
    const val SERVICE_BASE_URL = "https://a167-103-44-173-12.ngrok-free.app"

    const val PREFERENCES_NAME = "appPreferences"

    const val PREFERENCES_SIGNED_IN_KEY = "PREFERENCES_SIGNED_IN_KEY"
    const val PREFERENCES_AUTH_TYPE_KEY = "PREFERENCES_AUTH_TYPE_KEY"
    const val PREFERENCES_JWT_ACCESS_TOKEN_OR_SESSION_COOKIE_KEY = "PREFERENCES_JWT_ACCESS_TOKEN_OR_SESSION_COOKIE_KEY"
    const val PREFERENCES_JWT_REFRESH_TOKEN_KEY = "PREFERENCES_JWT_REFRESH_TOKEN_KEY"
    const val PREFERENCES_USERNAME_KEY = "PREFERENCES_USERNAME_KEY"
    const val PREFERENCES_PROFILE_PIC_KEY = "PREFERENCES_PROFILE_PIC_KEY"

    const val TYPE_PASSKEY_AUTH_REQ = "com.poulastaa.data.model.auth.PasskeyAuthReq"
    const val AUTH_TYPE_PASSKEY = "AUTH_TYPE_PASSKEY"

    const val TYPE_GOOGLE_AUTH_REQ = "com.poulastaa.data.model.auth.GoogleAuthReq"
    const val AUTH_TYPE_GOOGLE = "AUTH_TYPE_GOOGLE"

    const val TYPE_EMAIL_LOG_IN_REQ = "com.poulastaa.data.model.auth.EmailLoginReq"
    const val AUTH_TYPE_EMAIL_LOG_IN = "AUTH_TYPE_EMAIL_LOG_IN"


    const val TYPE_EMAIL_SIGN_UP_REQ = "com.poulastaa.data.model.auth.EmailSignUpReq"
    const val AUTH_TYPE_EMAIL_SIGN_UP = "AUTH_TYPE_EMAIL_SIGN_UP"

    const val AUTH_RESPONSE_PASSKEY_TYPE_SIGN_UP = "AUTH_RESPONSE_PASSKEY_TYPE_SIGN_UP"
}