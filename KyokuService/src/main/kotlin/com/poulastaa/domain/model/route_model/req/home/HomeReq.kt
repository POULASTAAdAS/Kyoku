package com.poulastaa.domain.model.route_model.req.home

import com.poulastaa.domain.model.DayType
import kotlinx.serialization.Serializable

@Serializable
data class HomeReq(
    val dayType: DayType,
)
