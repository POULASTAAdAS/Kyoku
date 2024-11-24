package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class CreatePlaylistPagerFilterTypeDto {
    ALL,
    ARTIST,
    ALBUM,
    PLAYLIST
}