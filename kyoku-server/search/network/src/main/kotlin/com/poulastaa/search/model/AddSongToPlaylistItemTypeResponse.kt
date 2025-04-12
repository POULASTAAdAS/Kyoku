package com.poulastaa.search.model

import kotlinx.serialization.Serializable

@Serializable
internal enum class AddSongToPlaylistItemTypeResponse {
    PLAYLIST,
    ALBUM,
    ARTIST,
    SONG
}