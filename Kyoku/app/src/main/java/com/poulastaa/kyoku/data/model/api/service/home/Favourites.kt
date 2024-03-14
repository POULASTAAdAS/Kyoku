package com.poulastaa.kyoku.data.model.api.service.home

import com.poulastaa.kyoku.data.model.api.service.ResponseSong
import kotlinx.serialization.Serializable

@Serializable
data class Favourites(
    val listOfSongs:List<ResponseSong> = emptyList()
)
