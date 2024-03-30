package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class AlbumPreview(
    val name: String = "",
    val listOfSongs: List<SongPreview> = emptyList()
)
