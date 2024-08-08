package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AlbumWithSongDto(
    val albumDto: AlbumDto = AlbumDto(),
    val listOfSong: List<SongDto> = emptyList(),
)