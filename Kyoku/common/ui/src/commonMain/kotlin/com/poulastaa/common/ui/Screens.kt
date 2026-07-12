package com.poulastaa.common.ui

import kotlinx.serialization.Serializable

sealed interface Screens {
    @Serializable
    object Loading : Screens

    @Serializable
    object AuthGraph : Screens

    @Serializable
    object SetupGraph : Screens

    @Serializable
    object MainGraph : Screens

    sealed interface AuthScreens : Screens {
        @Serializable
        object SignIn : AuthScreens

        @Serializable
        object SignUp : AuthScreens

        @Serializable
        data class ForgotPassword(val email: String? = null) : AuthScreens

        @Serializable
        data class ValidateOTP(val email: String) : AuthScreens

        @Serializable
        data class ResetPassword(val token: String) : AuthScreens
    }

    sealed interface SetupScreens : Screens {
        @Serializable
        object ImportPlaylist : SetupScreens

        @Serializable
        object SelectArtist : SetupScreens

        @Serializable
        object SelectGenre : SetupScreens
    }

    sealed interface MainScreens : Screens {
        @Serializable
        object Home : MainScreens

        @Serializable
        object Profile : MainScreens
    }
}
