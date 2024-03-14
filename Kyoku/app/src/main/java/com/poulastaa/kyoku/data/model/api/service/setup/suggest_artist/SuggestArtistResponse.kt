package com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist

import com.poulastaa.kyoku.data.model.api.service.ResponseArtist
import kotlinx.serialization.Serializable

@Serializable
data class SuggestArtistResponse(
    val status: ArtistResponseStatus,
    val artistList: List<ResponseArtist>
)
