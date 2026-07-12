package com.poulastaa.common.network.model

import com.poulastaa.common.domain.model.DtoUser
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUser(
    val userId: Long,
    val username: String,
    val email: String,
    val profilePictureUrl: String? = null,
    val isNewUser: Boolean = false,
) {
    fun toDto() = DtoUser(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profilePictureUrl,
        isNewUser = isNewUser,
    )
}
