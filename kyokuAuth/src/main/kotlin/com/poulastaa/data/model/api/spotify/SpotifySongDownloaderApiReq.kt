package com.poulastaa.data.model.api.spotify

import com.example.data.model.SpotifySong
import kotlinx.serialization.Serializable

@Serializable
data class SpotifySongDownloaderApiReq(
    val listOfSong: List<SpotifySong> = emptyList()
)
