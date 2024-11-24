package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryReq(
    val songId: Long,
    val otherId: Long,
    val type: HistoryTypeDto
)
