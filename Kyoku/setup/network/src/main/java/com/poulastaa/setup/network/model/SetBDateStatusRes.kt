package com.poulastaa.setup.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class SetBDateStatusRes {
    SUCCESS,
    FAILURE
}

@Serializable
data class SetBDateRes(
    val status: SetBDateStatusRes = SetBDateStatusRes.FAILURE,
)
