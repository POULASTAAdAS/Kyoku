package com.poulastaa.core.database.model

data class PlaylistResult(
    val playlistId: Long,
    val playlistName: String,
    val songId: Long,
    val songTitle: String,
    val songCoverImage: String,
    val artist: String,
)
