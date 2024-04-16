package com.poulastaa.data.model.db_table

data class PlaylistResult(
    val songId: Long,
    val playlistId: Long,
    val playlistName: String,
    val coverImage: String = "",
    val masterPlaylistUrl: String = "",
    val totalTime: String = "",
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val date: String = ""
)
