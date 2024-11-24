package com.poulastaa.play.presentation.add_new_album


sealed interface AddNewAlbumUiAction {
    data class Navigate(val screen: AddNewAlbumOtherScreen) : AddNewAlbumUiAction
}