package com.poulastaa.play.presentation.add_new_album

sealed interface AddAlbumUiEvent {
    data class OnAlbumClick(val albumId: Long) : AddAlbumUiEvent
    data object OnSearchToggle : AddAlbumUiEvent
    data object OnMassSelectToggle : AddAlbumUiEvent
    data class OnSearchQueryChange(val query: String) : AddAlbumUiEvent

    data class OnCheckChange(val id: Long, val status: Boolean) : AddAlbumUiEvent

    sealed interface ThreeDotEvent : AddAlbumUiEvent {
        data class OnClick(val id: Long) : ThreeDotEvent
        data class OnCloseClick(val id: Long) : ThreeDotEvent
        data class OnThreeDotItemClick(
            val id: Long,
            val operation: AddAlbumOperation
        ) : ThreeDotEvent
    }
}