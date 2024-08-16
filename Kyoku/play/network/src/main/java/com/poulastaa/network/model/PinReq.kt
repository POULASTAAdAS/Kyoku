package com.poulastaa.network.model

import com.poulastaa.core.domain.PinReqType
import kotlinx.serialization.Serializable

@Serializable
data class PinReq(
    val id: Long,
    val type: PinReqType
)
