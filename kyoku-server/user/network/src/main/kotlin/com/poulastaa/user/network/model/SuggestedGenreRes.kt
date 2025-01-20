package com.poulastaa.user.network.model

import com.poulastaa.core.network.model.ResponseGenre
import kotlinx.serialization.Serializable

@Serializable
data class SuggestedGenreRes(
    val list: List<ResponseGenre> = emptyList(),
)
