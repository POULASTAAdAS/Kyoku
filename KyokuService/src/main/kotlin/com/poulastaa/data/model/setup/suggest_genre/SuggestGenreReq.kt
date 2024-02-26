package com.poulastaa.data.model.setup.suggest_genre

import kotlinx.serialization.Serializable

@Serializable
data class SuggestGenreReq(
    val isSelectReq: Boolean,
    val alreadySendGenreList: List<String> = emptyList()
)
