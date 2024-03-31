package com.poulastaa.kyoku.data.model.api.service.home

import kotlinx.serialization.Serializable

@Serializable
data class AlbumPreview(
    val name: String = "",
    val points: Long = 0,
    val listOfSongs: List<SongPreview> = emptyList()
)
