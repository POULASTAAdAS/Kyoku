package com.poulastaa.core.domain

import com.poulastaa.core.domain.model.Song

data class AlbumData(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val listOfSong: List<Song> = emptyList()
)
