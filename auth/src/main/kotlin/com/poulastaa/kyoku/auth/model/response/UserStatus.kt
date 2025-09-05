package com.poulastaa.kyoku.auth.model.response

enum class UserStatus {
    USER_CREATED,
    USER_FOUND,
    USER_FOUND_NO_GENRE,
    USER_FOUND_NO_B_DATE,
    USER_FOUND_NO_ARTIST,
    USER_FOUND_NO_PLAYLIST,
    USER_NOT_FOUND,
}