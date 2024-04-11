package com.poulastaa.data.model.home

import com.poulastaa.data.model.common.IdType
import kotlinx.serialization.Serializable

@Serializable
data class Pinned(
    val id: Long = -1,
    val type: IdType = IdType.ERR
)