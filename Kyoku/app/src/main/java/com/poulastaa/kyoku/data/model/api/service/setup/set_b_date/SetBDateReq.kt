package com.poulastaa.kyoku.data.model.api.service.setup.set_b_date

import kotlinx.serialization.Serializable

@Serializable
data class SetBDateReq(
    val date: Long,
    val email: String
)
