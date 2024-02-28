package com.poulastaa.data.model.setup.artist

import kotlinx.serialization.Serializable

@Serializable
data class SuggestArtistResponse(
    val status: ArtistResponseStatus = ArtistResponseStatus.FAILURE,
    val artistList: List<ResponseArtist> = emptyList()
)
