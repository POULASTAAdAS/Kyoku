package com.poulastaa.kyoku.data.model.api.service.setup

import kotlinx.serialization.Serializable

@Serializable
data class SetBDateReq(
    val date: Long,
    val email: String
)
