package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
enum class TimeType {
    MORNING,
    DAY,
    NIGHT,
    MID_NIGHT
}