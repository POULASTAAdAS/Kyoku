
package com.poulastaa.data.model.common

import kotlinx.serialization.Serializable

@Serializable
enum class IdType {
    PLAYLIST,
    ALBUM,
    ARTIST,
    ERR
}