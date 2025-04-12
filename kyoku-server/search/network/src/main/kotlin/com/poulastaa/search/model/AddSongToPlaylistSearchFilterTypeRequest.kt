package com.poulastaa.search.model

import kotlinx.serialization.Serializable

@Serializable
internal enum class AddSongToPlaylistSearchFilterTypeRequest {
    ALL,
    ALBUM,
    SONG,
    ARTIST,
    PLAYLIST
}