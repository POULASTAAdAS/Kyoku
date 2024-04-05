
package com.poulastaa.data.model.pinned

import kotlinx.serialization.Serializable

@Serializable
enum class IdType {
    PLAYLIST,
    ALBUM,
    ARTIST,
    ERR
}