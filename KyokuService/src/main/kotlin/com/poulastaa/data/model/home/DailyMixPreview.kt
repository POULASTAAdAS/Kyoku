package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class DailyMixPreview(
    val listOfSongs: List<HomeResponseSong> = emptyList()
)
