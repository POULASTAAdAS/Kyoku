package com.poulastaa.auth.network.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordResponse(
    val status: UpdatePasswordStatus = UpdatePasswordStatus.USER_NOT_FOUND,
)