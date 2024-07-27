package com.poulastaa.utils

object Constants {
    private const val CURRENT_PROJECT_FOLDER = "G:"

    private const val PROFILE_PIC_ROOT_DIR = "$CURRENT_PROJECT_FOLDER/data/profilePic"

    private const val SECURITY_TYPE_EMAIL = "jwt-auth"
    private const val SECURITY_TYPE_SESSION = "session-auth"

    val SECURITY_LIST = arrayOf(SECURITY_TYPE_EMAIL, SECURITY_TYPE_SESSION)

    const val DEFAULT_PROFILE_PIC = "$PROFILE_PIC_ROOT_DIR/defaultProfilePic.png"

    const val ACCESS_TOKEN_CLAIM_KEY = "emailAccess"
    const val REFRESH_TOKEN_CLAIM_KEY = "emailRefresh"
    const val VERIFICATION_MAIL_TOKEN_CLAIM_KEY = "emailVerify"
    const val FORGOT_PASSWORD_MAIL_TOKEN_CLAIM_KEY = "forgotPassword"
    const val SUBMIT_NEW_PASSWORD_TOKEN_CLAIM_KEY = "submitPassword"

    const val GET_LOGIN_DATA_TOKEN_CLAIM_KEY = "getLoginData"

    const val SMS_EMAIL_GOOGLE_SMTP_HOST = "smtp.gmail.com"
    const val SMS_EMAIL_PORT = "587"

    const val SESSION_NAME_GOOGLE = "GOOGLE_USER_SESSION"

    const val DEFAULT_SESSION_MAX_AGE = 7L * 24 * 3600 // 7 days

    const val ACCESS_TOKEN_DEFAULT_TIME = 240000L // todo increase
    const val REFRESH_TOKEN_DEFAULT_TIME = 60L * 24 * 3600 // 60 days
    const val VERIFICATION_MAIL_TOKEN_TIME = 240000L // 4 minute
    const val FORGOT_PASSWORD_MAIL_TOKEN_TIME = 600000L // 10 minute
    const val SUBMIT_NEW_PASSWORD_TOKEN_TIME = 600000L // 10 minute
}