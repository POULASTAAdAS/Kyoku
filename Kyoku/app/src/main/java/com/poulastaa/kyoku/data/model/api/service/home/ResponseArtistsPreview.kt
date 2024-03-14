package com.poulastaa.kyoku.data.model.api.service.home

import com.poulastaa.kyoku.data.model.api.service.ResponseArtist
import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtistsPreview(
    val artist: ResponseArtist,
    val listOfSongs:List<SongPreview>
)
