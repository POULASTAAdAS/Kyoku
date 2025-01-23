package com.poulastaa.user.network.model

import com.poulastaa.core.network.model.UpsertReq
import kotlinx.serialization.Serializable

@Serializable
data class UpsertGenreReq(
    val list: List<UpsertReq<Int>>,
)
