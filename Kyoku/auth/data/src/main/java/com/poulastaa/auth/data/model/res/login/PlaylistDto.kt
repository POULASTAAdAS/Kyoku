package com.poulastaa.auth.data.model.res.login

import com.poulastaa.auth.data.model.res.login.SongDto
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistDto(
    val id: Long = -1,
    val name: String = "",
    val listOfSong: List<SongDto> = emptyList(),
)