package com.poulastaa.data.model

data class CreatePlaylistHelper(
    val user: CreatePlaylistHelperUser,
    val listOfSongId: List<Long>
)
