package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class CreatePlaylistPagerFilterTypeDto {
    ALL,
    ARTIST,
    ALBUM,
    PLAYLIST
}