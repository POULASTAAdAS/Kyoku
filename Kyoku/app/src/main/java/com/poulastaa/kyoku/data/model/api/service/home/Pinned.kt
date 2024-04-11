package com.poulastaa.kyoku.data.model.api.service.home

import com.poulastaa.kyoku.data.model.api.service.pinned.IdType
import kotlinx.serialization.Serializable

@Serializable
data class Pinned(
    val id: Long = -1,
    val type: IdType = IdType.ERR
)
