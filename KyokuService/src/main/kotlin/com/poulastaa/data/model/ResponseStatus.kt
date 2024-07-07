package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class ResponseStatus {
    SUCCESS,
    FAILURE
}