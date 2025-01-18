package com.poulastaa.user.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class SetBDateStatus {
    SUCCESS,
    FAILURE
}

@Serializable
data class SetBDateRes(
    val status: SetBDateStatus = SetBDateStatus.FAILURE,
)
