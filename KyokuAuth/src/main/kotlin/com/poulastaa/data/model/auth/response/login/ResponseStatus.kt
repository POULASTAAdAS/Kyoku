package com.poulastaa.data.model.auth.response.login

import kotlinx.serialization.Serializable

@Serializable
enum class ResponseStatus {
    SUCCESS,
    FAILURE
}