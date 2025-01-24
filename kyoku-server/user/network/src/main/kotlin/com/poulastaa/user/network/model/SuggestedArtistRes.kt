package com.poulastaa.user.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SuggestedArtistRes(
    val list: List<PrevArtistRes>,
)
