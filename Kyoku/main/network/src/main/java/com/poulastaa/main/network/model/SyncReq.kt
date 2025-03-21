package com.poulastaa.main.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SyncReq<T>(
    val idList: List<T>,
)
