package com.poulastaa.data.model.item

import com.poulastaa.data.model.common.IdType
import kotlinx.serialization.Serializable

@Serializable
data class ItemReq(
    val id: Long = -1,
    val type: IdType = IdType.ERR,
    val operation: ItemOperation = ItemOperation.ERR
)
