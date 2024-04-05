package com.poulastaa.kyoku.data.model.api.service.pinned

import kotlinx.serialization.Serializable

@Serializable
data class PinnedReq(
    val type: IdType = IdType.ERR,
    val id: Long = -1,
    val operation: PinnedOperation = PinnedOperation.ERR
)