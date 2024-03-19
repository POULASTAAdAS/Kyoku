package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtistsPreview(
    val artist: ResponseArtist = ResponseArtist(),
    val listOfSongs:List<SongPreview> = emptyList()
)
