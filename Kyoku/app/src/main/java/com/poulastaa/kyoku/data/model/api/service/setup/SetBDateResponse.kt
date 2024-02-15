package com.poulastaa.kyoku.data.model.api.service.setup

import kotlinx.serialization.Serializable

@Serializable
data class SetBDateResponse(
    val status: SetBDateResponseStatus
)
