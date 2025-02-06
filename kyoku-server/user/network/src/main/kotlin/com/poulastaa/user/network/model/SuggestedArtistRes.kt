package com.poulastaa.user.network.model

import com.poulastaa.core.network.model.ResponsePrevArtist
import kotlinx.serialization.Serializable

@Serializable
data class SuggestedArtistRes(
    val list: List<ResponsePrevArtist>,
)
