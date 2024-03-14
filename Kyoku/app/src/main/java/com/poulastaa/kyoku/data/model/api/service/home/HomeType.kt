package com.poulastaa.kyoku.data.model.api.service.home

import kotlinx.serialization.Serializable

@Serializable
enum class HomeType {
    NEW_USER_REQ, // signIn
    ALREADY_USER_REQ, // signUp
    DAILY_REFRESH_REQ
}