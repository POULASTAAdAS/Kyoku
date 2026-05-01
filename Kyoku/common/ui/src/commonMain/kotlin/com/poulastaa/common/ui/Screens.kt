package com.poulastaa.common.ui

import kotlinx.serialization.Serializable

sealed interface Screens {
    @Serializable
    object Loading : Screens

    sealed interface AuthScreens {
        @Serializable
        object SignIn : Screens

        @Serializable
        object SignUp : Screens

        @Serializable
        data class ForgotPassword(val email: String? = null) : Screens

        @Serializable
        data class ResetPassword(val token: String, val email: String) : Screens
    }

    sealed interface SetupScreens {
        @Serializable
        object ImportPlaylist : Screens

        @Serializable
        object SelectArtist : Screens

        @Serializable
        object SelectGenre : Screens
    }

    sealed interface MainScreens {
        @Serializable
        object Home : Screens

        @Serializable
        object Profile : Screens
    }
}