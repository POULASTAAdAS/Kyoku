package com.poulastaa.user.network.model

import com.poulastaa.core.network.model.ResponseGenre
import kotlinx.serialization.Serializable

@Serializable
data class SaveGenreRes(
    val list: List<ResponseGenre>,
)