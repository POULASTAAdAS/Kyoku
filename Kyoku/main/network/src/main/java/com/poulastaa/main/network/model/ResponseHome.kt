package com.poulastaa.main.network.model

import com.poulastaa.core.network.model.ResponseArtist
import com.poulastaa.core.network.model.ResponseFullAlbum
import com.poulastaa.core.network.model.ResponseFullPlaylist
import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseHome(
    val refresh: ResponseRefresh = ResponseRefresh(),

    val playlist: List<ResponseFullPlaylist> = emptyList(),
    val album: List<ResponseFullAlbum> = emptyList(),
    val artist: List<ResponseArtist> = emptyList(),
)
