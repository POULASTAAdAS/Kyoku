package com.poulastaa.data.model.setup.genre

import kotlinx.serialization.Serializable

@Serializable
data class SuggestGenreReq(
    val isSelectReq: Boolean,
    val alreadySendGenreList: List<String> = emptyList()
)
