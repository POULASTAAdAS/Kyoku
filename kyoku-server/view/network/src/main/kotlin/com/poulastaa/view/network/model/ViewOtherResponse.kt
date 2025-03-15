package com.poulastaa.view.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ViewOtherResponse(
    val heading: HeadingResponse,
    val songs: List<ResponseDetailPrevSong>,
)