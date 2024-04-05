
package com.poulastaa.kyoku.data.model.api.service.pinned

import kotlinx.serialization.Serializable

@Serializable
enum class IdType {
    PLAYLIST,
    ALBUM,
    ARTIST,
    ERR
}