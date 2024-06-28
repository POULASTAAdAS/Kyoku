package com.poulastaa.utils

object Constants {
    const val CURRENT_PROJECT_FOLDER = "G:"
    private const val PROFILE_PIC_ROOT_DIR = "/data/profilePic"

    const val MASTER_PLAYLIST_ROOT_DIR = "/data/master/"
    const val COVER_IMAGE_ROOT_DIR = "/data/coverImage/"
    const val ARTIST_IMAGE_ROOT_DIR = "/data/artist/"

    const val PLAYLIST = "/songs/"

    const val SONG_128 = "/data/128"
    const val SONG_320 = "/data/320"

    const val DEFAULT_PROFILE_PIC = "$PROFILE_PIC_ROOT_DIR/defaultProfilePic.png"

    const val ISSUER = "https://accounts.google.com"

    const val SESSION_NAME_GOOGLE = "GOOGLE_USER_SESSION"

    const val SMS_EMAIL_GOOGLE_SMTP_HOST = "smtp.gmail.com"
    const val SMS_EMAIL_PORT = "587"

    const val ACCESS_TOKEN_CLAIM_KEY = "emailAccess"
    const val REFRESH_TOKEN_CLAIM_KEY = "emailRefresh"

    const val DEFAULT_SESSION_MAX_AGE = 7L * 24 * 3600 // 7 days

    const val ACCESS_TOKEN_DEFAULT_TIME = 240000L // todo increase
    const val REFRESH_TOKEN_DEFAULT_TIME = 60L * 24 * 3600 // 60 days

    private const val SECURITY_TYPE_EMAIL = "jwt-auth"
    private const val SECURITY_TYPE_GOOGLE = "google-auth"

    val SECURITY_LIST = arrayOf(SECURITY_TYPE_EMAIL, SECURITY_TYPE_GOOGLE)
}