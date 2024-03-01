package com.poulastaa.data.model.utils

data class CreatePlaylistHelper(
    val typeHelper: UserTypeHelper,
    val listOfSongId: List<Long>,
    val playlistName:String
)
