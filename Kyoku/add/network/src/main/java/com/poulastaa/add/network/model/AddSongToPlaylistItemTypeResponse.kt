package com.poulastaa.add.network.model

import kotlinx.serialization.Serializable

@Serializable
internal enum class AddSongToPlaylistItemTypeResponse {
    PLAYLIST,
    ALBUM,
    ARTIST,
    SONG
}