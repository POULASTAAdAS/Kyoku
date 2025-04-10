package com.poulastaa.kyoku.navigation

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.view.presentation.saved.ViewSavedUiItemType
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

    sealed interface Core : Screens {
        @Serializable
        data class Main(
            val isInitial: Boolean = false,
            val isHome: Boolean = true,
        ) : Screens

        @Serializable
        data object Settings : Screens

        @Serializable
        data object Profile : Screens

        @Serializable
        data object History : Screens
    }

    sealed interface View : Screens {
        @Serializable
        data class Artist(val artistId: ArtistId) : Screens

        @Serializable
        data class Other(
            val otherId: Long,
            val type: ViewType,
        ) : Screens

        @Serializable
        data class Saved(val type: ViewSavedUiItemType) : Screens
    }

    sealed interface Explore : Screens {
        @Serializable
        data class AllFromArtist(val artistId: ArtistId) : Screens

        @Serializable
        data object ExploreAlbum : Screens

        @Serializable
        data object ExploreArtist : Screens
    }

    sealed interface Add : Screens {
        @Serializable
        data object CreatePlaylist : Screens
    }
}