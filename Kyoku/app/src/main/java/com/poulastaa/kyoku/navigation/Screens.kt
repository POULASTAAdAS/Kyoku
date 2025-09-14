package com.poulastaa.kyoku.navigation

import com.poulastaa.core.presentation.Email
import com.poulastaa.core.presentation.JWTToken
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
        data class ForgotPassword(val email: Email? = null) : Screens

        @Serializable
        data object VerifyEmail : Screens

        @Serializable
        data class CreateNewPassword(val token: JWTToken) : Screens
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
}