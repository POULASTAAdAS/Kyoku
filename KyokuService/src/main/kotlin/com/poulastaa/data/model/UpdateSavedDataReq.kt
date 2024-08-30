package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSavedDataReq(
    val list: List<Long>,
    val type: UpdateSavedDataType,
)
