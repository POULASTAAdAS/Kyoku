package com.poulastaa.kyoku.navigation

import kotlinx.serialization.Serializable

sealed interface Screens {
    sealed interface Auth : Screens {
        @Serializable
        data object Intro : Screens

        @Serializable
        data object EmailLogIn : Screens

        @Serializable
        data object EmailSignUp : Screens

        @Serializable
        data class ForgotPassword(val email: String? = null) : Screens
    }
}