package com.poulastaa.data.model.setup.suggest_genre

import kotlinx.serialization.Serializable

@Serializable
data class SuggestGenreReq(
    val countryName: String,
    val alreadySendGenreList: List<String>?
)
