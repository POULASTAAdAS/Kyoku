package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class ResponseAlbumPreview(
    val name:String = "",
    val listOfSongs:List<HomeResponseSong> = emptyList()
)
