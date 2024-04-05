package com.poulastaa.kyoku.data.model.api.service.item

import com.poulastaa.kyoku.data.model.api.service.pinned.IdType
import kotlinx.serialization.Serializable

@Serializable
data class ItemReq(
    val id: Long = -1,
    val type: IdType = IdType.ERR,
    val operation: ItemOperation = ItemOperation.ERR
)
