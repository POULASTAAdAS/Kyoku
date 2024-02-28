package com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist

import kotlinx.serialization.Serializable

@Serializable
data class SuggestArtistReq(
    val isSelected: Boolean,
    val alreadySendArtistList: List<String>
)
