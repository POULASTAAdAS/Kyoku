package com.poulastaa.data.model

data class CreatePlaylistHelper(
    val user: UserTypeHelper,
    val listOfSongId: List<Long>
)
