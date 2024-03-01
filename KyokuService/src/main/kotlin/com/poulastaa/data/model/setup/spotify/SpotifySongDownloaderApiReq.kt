package com.poulastaa.data.model.setup.spotify

import kotlinx.serialization.Serializable

@Serializable
data class SpotifySongDownloaderApiReq(
    val listOfSong: List<SpotifySong> = emptyList()
)
