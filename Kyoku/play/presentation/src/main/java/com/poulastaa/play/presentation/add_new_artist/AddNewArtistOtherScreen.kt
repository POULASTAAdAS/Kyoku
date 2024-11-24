package com.poulastaa.play.presentation.add_new_artist

sealed interface AddNewArtistOtherScreen {
    data class ViewArtist(val id: Long) : AddNewArtistOtherScreen
}