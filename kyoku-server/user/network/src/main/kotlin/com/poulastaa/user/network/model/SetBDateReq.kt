package com.poulastaa.user.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SetBDateReq(
    val date: String,
)
