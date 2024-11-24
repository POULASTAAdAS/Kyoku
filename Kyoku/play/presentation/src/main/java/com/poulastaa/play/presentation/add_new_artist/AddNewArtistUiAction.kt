package com.poulastaa.play.presentation.add_new_artist

sealed interface AddNewArtistUiAction {
    data class Navigate(val screen: AddNewArtistOtherScreen) : AddNewArtistUiAction
}