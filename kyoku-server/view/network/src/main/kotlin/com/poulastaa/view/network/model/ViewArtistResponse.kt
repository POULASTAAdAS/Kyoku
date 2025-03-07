package com.poulastaa.view.network.model

import com.poulastaa.core.network.model.ResponsePrevArtist
import com.poulastaa.core.network.model.ResponsePrevSong
import kotlinx.serialization.Serializable

@Serializable
data class ViewArtistResponse(
    val artist: ResponsePrevArtist,
    val songs: List<ResponsePrevSong>,
)
