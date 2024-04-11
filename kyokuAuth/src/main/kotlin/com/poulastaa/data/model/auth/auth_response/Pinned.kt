package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
data class Pinned(
    val id: Long = -1,
    val type: IdType = IdType.ERR
)
