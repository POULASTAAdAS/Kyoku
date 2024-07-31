package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserAuthStatus {
    USER_FOUND_HOME,
    USER_FOUND_SET_GENRE,
    USER_FOUND_SET_ARTIST,
    USER_NOT_FOUND,
}