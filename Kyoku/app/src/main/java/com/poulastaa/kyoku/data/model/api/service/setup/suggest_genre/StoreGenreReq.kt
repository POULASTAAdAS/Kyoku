package com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre

import kotlinx.serialization.Serializable

@Serializable
data class StoreGenreReq(
    val data: List<String>
)
