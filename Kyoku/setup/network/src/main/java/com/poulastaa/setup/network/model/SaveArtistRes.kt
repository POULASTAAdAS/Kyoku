package com.poulastaa.setup.network.model

import com.poulastaa.core.network.model.ResponseArtist
import kotlinx.serialization.Serializable

@Serializable
data class SaveArtistRes(
    val list: List<ResponseArtist>,
)
