package com.poulastaa.kyoku.data.model.api.service.home

import kotlinx.serialization.Serializable

@Serializable
enum class TimeType {
    MORNING,
    DAY,
    NIGHT,
    MID_NIGHT
}