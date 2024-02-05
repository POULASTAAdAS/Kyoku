package com.poulastaa.data.model.spotify

import kotlinx.serialization.Serializable

@Serializable
data class SpotifySongDownloaderApiReq(
    val listOfSong: List<SpotifySong> = emptyList()
)
