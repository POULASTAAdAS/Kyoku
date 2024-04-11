package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
enum class IdType {
    PLAYLIST,
    ALBUM,
    ARTIST,
    ERR
}