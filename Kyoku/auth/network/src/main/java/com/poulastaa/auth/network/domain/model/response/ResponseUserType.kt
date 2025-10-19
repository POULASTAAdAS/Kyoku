package com.poulastaa.auth.network.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
enum class ResponseUserType {
    EMAIL,
    GOOGLE,
    DEFAULT
}