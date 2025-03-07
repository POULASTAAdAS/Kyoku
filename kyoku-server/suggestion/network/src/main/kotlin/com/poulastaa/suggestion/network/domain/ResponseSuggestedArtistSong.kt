package com.poulastaa.suggestion.network.domain

import com.poulastaa.core.network.model.ResponsePrevArtist
import com.poulastaa.core.network.model.ResponsePrevSong
import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseSuggestedArtistSong(
    val artist: ResponsePrevArtist = ResponsePrevArtist(),
    val prevSongs: List<ResponsePrevSong> = emptyList(),
)
