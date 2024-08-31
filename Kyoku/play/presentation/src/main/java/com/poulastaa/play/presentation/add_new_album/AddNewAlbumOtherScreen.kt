package com.poulastaa.play.presentation.add_new_album

sealed interface AddNewAlbumOtherScreen {
    data class ViewAlbum(val id: Long) : AddNewAlbumOtherScreen
    data class AddSongToPlaylist(val id: Long) : AddNewAlbumOtherScreen
}