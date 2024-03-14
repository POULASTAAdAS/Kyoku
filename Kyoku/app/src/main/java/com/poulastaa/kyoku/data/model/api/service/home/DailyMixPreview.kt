package com.poulastaa.kyoku.data.model.api.service.home

import kotlinx.serialization.Serializable

@Serializable
data class DailyMixPreview(
    val listOfSongs: List<SongPreview> = emptyList()
)
