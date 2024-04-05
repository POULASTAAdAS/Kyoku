package com.poulastaa.data.model.pinned

import kotlinx.serialization.Serializable

@Serializable
data class PinnedReq(
    val type: IdType = IdType.ERR,
    val id: Long = -1,
    val operation: PinnedOperation = PinnedOperation.ERR
)