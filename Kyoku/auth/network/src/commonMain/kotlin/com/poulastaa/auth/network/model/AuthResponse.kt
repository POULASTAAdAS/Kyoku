package com.poulastaa.auth.network.model

import com.poulastaa.auth.domain.model.DtoAuthResponse
import com.poulastaa.common.network.model.ResponseTokens
import com.poulastaa.common.network.model.ResponseUser
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val user: ResponseUser,
    val tokens: ResponseTokens,
){
    fun toDto() = DtoAuthResponse(
        user = user.toDto(),
        tokens = tokens.toDto(),
    )
}
