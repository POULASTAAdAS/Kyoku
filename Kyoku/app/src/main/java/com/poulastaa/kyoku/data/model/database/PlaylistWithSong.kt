package com.poulastaa.kyoku.data.model.database

import androidx.room.Embedded

data class SongInfo(
    val id: Long,
    val coverImage: String,
    val title: String,
    val artist: String
)

data class PlaylistSongInfo(
    @Embedded val song: SongInfo,
    val name: String,
)

data class PlaylistWithSongs(
    @Embedded val song: PlaylistSongInfo
)