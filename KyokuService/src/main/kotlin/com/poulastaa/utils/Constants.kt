package com.poulastaa.utils

object Constants {
    const val MASTER_PLAYLIST_ROOT_DIR = "F:/songs/master/"
    const val COVER_IMAGE_ROOT_DIR = "F:/songs/coverPhoto/"
    const val PROFILE_PIC_ROOT_DIR = "F:/songs/profilePic"

    const val DEFAULT_PROFILE_PIC = "$PROFILE_PIC_ROOT_DIR/defaultProfilePic.png"

    const val BASE_URL = "https://e04a-103-192-117-134.ngrok-free.app"

    const val ISSUER = "https://accounts.google.com"

    const val SESSION_NAME_GOOGLE = "GOOGLE_USER_SESSION"
    const val SESSION_NAME_PASSKEY = "PASSKEY_USER_SESSION"

    const val AUTH_RESPONSE_PASSKEY_TYPE_LOGIN = "AUTH_RESPONSE_PASSKEY_TYPE_LOGIN"
    const val AUTH_RESPONSE_PASSKEY_TYPE_SIGN_UP = "AUTH_RESPONSE_PASSKEY_TYPE_SIGN_UP"

    const val AUTH_TYPE_GOOGLE = "AUTH_TYPE_GOOGLE"
    const val AUTH_TYPE_EMAIL_SIGN_UP = "AUTH_TYPE_EMAIL_SIGN_UP"
    const val AUTH_TYPE_EMAIL_LOG_IN = "AUTH_TYPE_EMAIL_LOG_IN"
    const val AUTH_TYPE_PASSKEY = "AUTH_TYPE_PASSKEY"

    const val SMS_EMAIL_GOOGLE_SMTP_HOST = "smtp.gmail.com"
    const val SMS_EMAIL_PORT = "587"

    const val ACCESS_TOKEN_CLAIM_KEY = "emailAccess"
    const val REFRESH_TOKEN_CLAIM_KEY = "emailRefresh"
    const val VERIFICATION_MAIL_TOKEN_CLAIM_KEY = "emailVerify"
    const val FORGOT_PASSWORD_MAIL_TOKEN_CLAIM_KEY = "forgotPassword"

    const val DEFAULT_SESSION_MAX_AGE = 7L * 24 * 3600 // 7 days

    const val ACCESS_TOKEN_DEFAULT_TIME = 240000L // todo increase
    const val REFRESH_TOKEN_DEFAULT_TIME = 60L * 24 * 3600 // 60 days
    const val VERIFICATION_MAIL_TOKEN_TIME = 240000L // 4 minute
    const val FORGOT_PASSWORD_MAIL_TOKEN_TIME = 240000L // 4 minute
}