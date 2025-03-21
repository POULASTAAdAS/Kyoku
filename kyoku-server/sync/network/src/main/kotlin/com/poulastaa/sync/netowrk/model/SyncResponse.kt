package com.poulastaa.sync.netowrk.model

import kotlinx.serialization.Serializable

@Serializable
data class SyncResponse<T>(
    val removeIdList: List<Long>,
    val newData: List<T>,
)
