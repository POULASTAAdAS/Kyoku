package com.poulastaa.data.model

data class CreatePlaylistHelper(
    val user: FindUserType,
    val listOfSongId: List<Long>
)
