package com.poulastaa.sync.netowrk.model

import kotlinx.serialization.Serializable

@Serializable
data class SyncReq<T>(
    val idList: List<T>,
)
