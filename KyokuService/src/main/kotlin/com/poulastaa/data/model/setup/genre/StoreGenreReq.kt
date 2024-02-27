package com.poulastaa.data.model.setup.genre

import kotlinx.serialization.Serializable

@Serializable
data class StoreGenreReq(
    val data: List<String>
)
