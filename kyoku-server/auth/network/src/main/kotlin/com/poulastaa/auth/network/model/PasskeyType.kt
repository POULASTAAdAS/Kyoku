package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class PasskeyType {
    LOGIN,
    SIGNUP
}