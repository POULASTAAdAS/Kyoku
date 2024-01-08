package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SpotifySongDownloaderApiReq(
    val listOfSong: List<SpotifySong> = emptyList()
)
