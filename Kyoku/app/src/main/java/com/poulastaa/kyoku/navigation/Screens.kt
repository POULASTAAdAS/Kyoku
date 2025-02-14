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

    sealed interface SetUp : Screens {
        @Serializable
        data object ImportSpotifyPlaylist : Screens

        @Serializable
        data object SetBirthDate : Screens

        @Serializable
        data object PickGenre : Screens

        @Serializable
        data object PickArtist : Screens
    }

    sealed class Core : Screens {
        @Serializable
        data class Main(val isInitial: Boolean = false) : Screens

        @Serializable
        data object Home : Screens

        @Serializable
        data object Library : Screens

        @Serializable
        data object Settings : Screens
    }
}