package com.poulastaa.kyoku.navigation

import kotlinx.serialization.Serializable

sealed interface Screens {
    @Serializable
    data object Auth : Screens

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