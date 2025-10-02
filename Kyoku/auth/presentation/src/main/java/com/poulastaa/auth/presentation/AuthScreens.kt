package com.poulastaa.auth.presentation

import com.poulastaa.core.presentation.Email
import kotlinx.serialization.Serializable

sealed interface AuthScreens {
    @Serializable
    data object Intro : AuthScreens

    @Serializable
    data class EmailSignUp(val email: Email? = null) : AuthScreens

    @Serializable
    data class ForgotPassword(val email: Email? = null) : AuthScreens

    @Serializable
    data class Verify(val email: Email? = null) : AuthScreens
}