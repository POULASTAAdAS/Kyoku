package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class UpdateSavedDataType {
    ALBUM,
    PLAYLIST,
    ARTIST
}