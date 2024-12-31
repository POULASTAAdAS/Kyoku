package com.poulastaa.core.domain.model

data class PlaylistFullDto(
    val playlist: PlaylistDto,
    val listOfSong: List<SongDto> = emptyList(),
)
