package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeReq(
    val type:HomeType,
    val time:TimeType
)
