package com.poulastaa.suggestion.network.model

import com.poulastaa.core.network.model.ResponsePrevArtist
import com.poulastaa.core.network.model.ResponsePrevSong
import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseRefresh(
    val prevPopularSongMix: List<ResponsePrevSong> = emptyList(),
    val prevPopularArtistMix: List<ResponsePrevSong> = emptyList(),
    val prevOldGem: List<ResponsePrevSong> = emptyList(),

    val suggestedArtist: List<ResponsePrevArtist> = emptyList(),
    val suggestedAlbum: List<ResponsePrevAlbum> = emptyList(),
    val suggestedArtistSong: List<ResponseSuggestedArtistSong> = emptyList(),
)
