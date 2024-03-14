package com.poulastaa.kyoku.data.model.api.service.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeReq(
    val type: HomeType,
    val time: TimeType,
    val isOldEnough: Boolean
)
