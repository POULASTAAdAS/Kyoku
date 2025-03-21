package com.poulastaa.sync.netowrk.model

import kotlinx.serialization.Serializable

@Serializable
enum class SyncType {
    SYNC_ALBUM,
    SYNC_PLAYLIST,
    SYNC_PLAYLIST_SONGS,
    SYNC_ARTIST,
    SYNC_FAVOURITE
}