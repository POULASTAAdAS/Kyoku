package com.poulastaa.data.model.db_table

data class PlaylistResult(
    val playlistId: Long,
    val playlistName: String,
    val coverImage: String = "",
    val masterPlaylistUrl: String = "",
    val totalTime: String = "",
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val genre: String = "",
    val composer: String = "",
    val publisher: String = "",
    val albumArtist: String = "",
    val description: String = "",
    val track: String = "",
    val date: String = ""
)
