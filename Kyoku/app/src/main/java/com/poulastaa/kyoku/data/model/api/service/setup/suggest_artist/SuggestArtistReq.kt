package com.poulastaa.data.model.setup.artist

import kotlinx.serialization.Serializable

@Serializable
data class SuggestArtistReq(
    val isSelected: Boolean,
    val alreadySendArtistList: List<String>
)
