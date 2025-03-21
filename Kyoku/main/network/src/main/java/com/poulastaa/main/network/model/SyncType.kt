package com.poulastaa.main.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class SyncType {
    SYNC_ALBUM,
    SYNC_PLAYLIST,
    SYNC_PLAYLIST_SONGS,
    SYNC_ARTIST,
    SYNC_FAVOURITE
}