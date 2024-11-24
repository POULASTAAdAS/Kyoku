package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class GetDataReq(
    val id: Long = -1,
    val listOfId: List<Long> = emptyList(),
    val type: GetDataType,
)
