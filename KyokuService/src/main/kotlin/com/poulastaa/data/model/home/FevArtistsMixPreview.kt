package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class FevArtistsMixPreview(
    val listOfSong: List<HomeResponseSong> = emptyList()
)
