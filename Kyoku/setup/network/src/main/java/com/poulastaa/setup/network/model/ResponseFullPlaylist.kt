package com.poulastaa.setup.network.model

import com.poulastaa.core.network.model.ResponsePlaylist
import com.poulastaa.core.network.model.ResponseSong
import kotlinx.serialization.Serializable

@Serializable
data class ResponseFullPlaylist(
    val playlist: ResponsePlaylist = ResponsePlaylist(),
    val listOfSong: List<ResponseSong> = emptyList(),
)
