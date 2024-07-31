package com.poulastaa.data.model

import com.poulastaa.domain.model.UserType
import kotlinx.serialization.Serializable

@Serializable
data class GetDataReq(
    val token: String,
    val userType: UserType,
)
