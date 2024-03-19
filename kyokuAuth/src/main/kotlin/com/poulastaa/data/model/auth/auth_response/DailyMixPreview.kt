package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
data class DailyMixPreview(
    val listOfSongs: List<SongPreview> = emptyList()
)
