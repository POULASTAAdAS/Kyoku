package com.poulastaa.network.model

import com.poulastaa.core.domain.model.DayType
import kotlinx.serialization.Serializable

@Serializable
data class NewHomeReq(
    val dayType: DayType,
)
