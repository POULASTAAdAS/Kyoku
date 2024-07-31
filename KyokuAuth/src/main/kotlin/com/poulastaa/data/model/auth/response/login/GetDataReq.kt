package com.poulastaa.data.model.auth.response.login

import com.poulastaa.data.model.UserType
import kotlinx.serialization.Serializable

@Serializable
data class GetDataReq(
    val token: String,
    val userType: UserType,
)