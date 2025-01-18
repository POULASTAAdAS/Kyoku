package com.poulastaa.setup.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SetBDateReq(
    val date: String,
)
