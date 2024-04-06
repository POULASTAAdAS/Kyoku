package com.poulastaa.kyoku.data.model.screens.home

import com.poulastaa.kyoku.data.model.screens.common.ItemsType

sealed class HomeUiEvent {
    data class EmitToast(val message: String) : HomeUiEvent()
    data object SomethingWentWrong : HomeUiEvent()

    data class ItemClick(
        val type: ItemsType,
        val id: Long = 0,
        val name: String = "name",
        val isApiCall: Boolean = false
    ) : HomeUiEvent()

    data class ItemLongClick(
        val type: HomeLongClickType,
        val id: Long = 0,
        val name: String = "name",
    ) : HomeUiEvent()

    sealed class BottomSheetItemClick : HomeUiEvent() {
        data class PlaySong(val id: Long, val type: SongType) : BottomSheetItemClick()

        data class AddToFavourite(val id: Long, val type: SongType) : BottomSheetItemClick()
        data class RemoveFromFavourite(val id: Long) : BottomSheetItemClick()

        data class ViewArtist(
            val id: Long,
            val type: SongType
        ) : BottomSheetItemClick()

        data class RemoveFromListenHistory(val id: Long) : BottomSheetItemClick()
        data class HideSong(val id: Long) : BottomSheetItemClick()

        data class AddToPlaylist(val id: Long, val type: HomeLongClickType) : BottomSheetItemClick()

        data class PlayAlbum(val id: Long) : BottomSheetItemClick()
        data class AddToLibraryAlbum(val id: Long) : BottomSheetItemClick()
        data class DownloadAlbum(val id: Long) : BottomSheetItemClick()

        data object PlayArtistMix : BottomSheetItemClick()
        data object DownloadArtistMix : BottomSheetItemClick()

        data object PlayDailyMix : BottomSheetItemClick()
        data object DownloadDailyMix : BottomSheetItemClick()

        data object CancelClicked : BottomSheetItemClick()

        data class CreatePlaylistText(val text: String) : BottomSheetItemClick()
        data object CreatePlaylistSave : BottomSheetItemClick()
        data object CreatePlaylistCancel : BottomSheetItemClick()
    }
}


enum class SongType {
    HISTORY_SONG,
    ARTIST_SONG
}