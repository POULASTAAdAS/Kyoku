package com.example.data.model.api.req

import com.example.data.model.SpotifySong
import kotlinx.serialization.Serializable

@Serializable
data class SpotifySongDownloaderApiReq(
    val listOfSong: List<SpotifySong> = emptyList()
)
