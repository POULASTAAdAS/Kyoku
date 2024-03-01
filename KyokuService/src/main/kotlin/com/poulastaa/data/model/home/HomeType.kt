package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
enum class HomeType {
    NEW_USER_REQ, // signIn
    ALREADY_USER_REQ, // signUp
    DALY_REFRESH_REQ
}