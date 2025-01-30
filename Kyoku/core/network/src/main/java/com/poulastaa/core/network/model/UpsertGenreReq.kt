package com.poulastaa.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UpsertGenreReq(
    val data: UpsertReq<Int>,
)
