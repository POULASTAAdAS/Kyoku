package com.poulastaa.setup.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SuggestedArtistRes(
    val list: List<PrevArtist>
)
