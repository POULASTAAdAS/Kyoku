package com.poulastaa.setup.network.model.res

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistDto(
    val id: Long,
    val name: String,
    val listOfSong: List<SongDto>,
)
