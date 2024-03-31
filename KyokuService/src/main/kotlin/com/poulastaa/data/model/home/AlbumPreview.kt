package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class AlbumPreview(
    val id: Long = 0,
    val name: String = "",
    val points: Long = 0,
    val listOfSongs: List<SongPreview> = emptyList()
)
