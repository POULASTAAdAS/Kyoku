package com.poulastaa.kyoku.data.model.screens.library

sealed class LibraryUiEvent {
    data class EmitToast(val message: String) : LibraryUiEvent()
    data object SomethingWentWrong : LibraryUiEvent()

    data object HideBottomSheet : LibraryUiEvent()

    sealed class FilterChipClick : LibraryUiEvent() {
        data object PlaylistType : FilterChipClick()
        data object ArtistType : FilterChipClick()
        data object AlbumType : FilterChipClick()
    }

    sealed class ItemClick : LibraryUiEvent() {
        data object SortTypeClick : ItemClick()

        data object CreatePlaylistClick : ItemClick()
        data object AddAlbumClick : ItemClick()
        data object AddArtistClick : ItemClick()

        data class PlaylistLongClick(val name: String) : ItemClick()
        data class PlaylistClick(val name: String) : ItemClick()

        data class AlbumLongClick(val name: String) : ItemClick()
        data class AlbumClick(val name: String) : ItemClick()

        data class ArtistLongClick(val id: Long, val name: String) : ItemClick()
        data class ArtistClick(val id: Long, val name: String) : ItemClick()

        data object FavouriteLongClick : ItemClick()
        data object FavouriteClick : ItemClick()
    }

    sealed class BottomSheetItemClick : ItemClick() {
        data class AddClick(val type: String, val name: String) : BottomSheetItemClick()
        data object RemoveClick : BottomSheetItemClick()
        data object DeleteClick : BottomSheetItemClick()
    }

    sealed class DeleteDialogClick : ItemClick() {
        data object DeleteYes : DeleteDialogClick()
        data object DeleteNo : DeleteDialogClick()
    }
}