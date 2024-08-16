package com.poulastaa.domain.model.route_model.req.pin

import kotlinx.serialization.Serializable

@Serializable
data class PinReq(
    val id: Long,
    val type: PinReqType
)
