package com.poulastaa.play.presentation.root_drawer.library

import com.poulastaa.play.presentation.root_drawer.library.model.LibraryFilterType

sealed interface LibraryUiEvent {
    data object OnSearchClick : LibraryUiEvent

    data class ToggleFilterType(val type: LibraryFilterType) : LibraryUiEvent
    data object ToggleView : LibraryUiEvent

    sealed interface OnClick : LibraryUiEvent {
        data object Favourite : OnClick
        data class Album(val id: Long) : OnClick
        data class Playlist(val id: Long) : OnClick
        data class Artist(val id: Long) : OnClick

        data object PinnedHeader : OnClick
        data object PlaylistHeader : OnClick
        data object AlbumHeader : OnClick
        data object ArtistHeader : OnClick
    }

    data class OnItemLongClick(
        val id: Long,
        val type: LibraryBottomSheetLongClickType
    ) : LibraryUiEvent

    data object OnItemBottomSheetCancel : LibraryUiEvent

    sealed interface BottomSheetUiEvent : LibraryUiEvent {
        sealed interface Favourite : BottomSheetUiEvent {
            data class Play(val id: Long) : Favourite
            data class Pin(val id: Long) : Favourite
            data class UnPin(val id: Long) : Favourite
            data class View(val id: Long) : Favourite
            data class Download(val id: Long) : Favourite
        }

        sealed interface Album : BottomSheetUiEvent {
            data class Play(val id: Long) : Album
            data class Pin(val id: Long) : Album
            data class UnPin(val id: Long) : Album
            data class View(val id: Long) : Album
            data class Download(val id: Long) : Album
            data class Remove(val id: Long) : Album
        }

        sealed interface Playlist : BottomSheetUiEvent {
            data class Play(val id: Long) : Playlist
            data class Pin(val id: Long) : Playlist
            data class UnPin(val id: Long) : Playlist
            data class View(val id: Long) : Playlist
            data class Download(val id: Long) : Playlist
            data class Remove(val id: Long) : Playlist
        }

        sealed interface Artist : BottomSheetUiEvent {
            data class Pin(val id: Long) : Artist
            data class UnPin(val id: Long) : Artist
            data class View(val id: Long) : Artist
            data class UnFollow(val id: Long) : Artist
        }
    }
}