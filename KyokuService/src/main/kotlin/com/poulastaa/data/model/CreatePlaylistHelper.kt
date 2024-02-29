package com.poulastaa.data.model

data class CreatePlaylistHelper(
    val typeHelper: UserTypeHelper,
    val listOfSongId: List<Long>,
    val playlistName:String
)
