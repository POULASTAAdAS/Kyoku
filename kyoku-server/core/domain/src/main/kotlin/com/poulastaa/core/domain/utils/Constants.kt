package com.poulastaa.core.domain.utils

object Constants {
    const val CURRENT_PROJECT_FOLDER = "G:"

    const val SMS_EMAIL_GOOGLE_SMTP_HOST = "smtp.gmail.com"
    const val SMS_EMAIL_PORT = "587"

    const val SESSION_NAME_GOOGLE = "GOOGLE_USER_SESSION"

    const val DEFAULT_SESSION_MAX_AGE = 7L * 24 * 3600 // 7 days

    private const val SECURITY_TYPE_EMAIL = "jwt-auth"
    private const val SECURITY_TYPE_GOOGLE = "google-auth"

    val SECURITY_LIST = arrayOf(SECURITY_TYPE_EMAIL, SECURITY_TYPE_GOOGLE)

    const val POSTER_PARAM = "poster"
}