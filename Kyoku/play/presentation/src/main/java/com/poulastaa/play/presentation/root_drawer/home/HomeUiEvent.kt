package com.poulastaa.play.presentation.root_drawer.home

import android.content.Context
import com.poulastaa.play.presentation.root_drawer.model.HomeItemClickType

sealed interface HomeUiEvent {
    data class OnItemClick(
        val id: Long? = null,
        val itemClickType: HomeItemClickType,
    ) : HomeUiEvent

    data class OnItemLongClick(
        val id: Long = -1L,
        val artistId: Long = -1L,
        val itemClickType: HomeItemClickType,
        val context: Context,
    ) : HomeUiEvent

    sealed interface BottomSheetUiEvent : HomeUiEvent {
        data object Cancel : BottomSheetUiEvent

        data object PlayPopularSongMix : BottomSheetUiEvent
        data object PlayOldGem : BottomSheetUiEvent
        data object PlayFavouriteArtistMix : BottomSheetUiEvent

        data object AddAsPlaylistPopularSongMix : BottomSheetUiEvent
        data object AddAsPlaylistOldGem : BottomSheetUiEvent
        data object AddAsFavouriteArtistMix : BottomSheetUiEvent

        data object DownloadPopularSongMix : BottomSheetUiEvent
        data object DownloadOldGem : BottomSheetUiEvent
        data object DownloadFavouriteArtistMix : BottomSheetUiEvent

        data class FollowArtist(val id: Long) : BottomSheetUiEvent
        data class UnFollowArtist(val id: Long) : BottomSheetUiEvent
        data class ExploreArtist(val id: Long) : BottomSheetUiEvent

        data class PlayAlbum(val id: Long) : BottomSheetUiEvent
        data class SaveAlbum(val id: Long) : BottomSheetUiEvent
        data class RemoveSavedAlbum(val id: Long) : BottomSheetUiEvent

        data class PlaySong(val id: Long) : BottomSheetUiEvent
        data class RemoveFromQueue(val id: Long) : BottomSheetUiEvent
        data class PlayNextOnQueue(val id: Long) : BottomSheetUiEvent
        data class PlayLastOnQueue(val id: Long) : BottomSheetUiEvent
        data class AddSongToPlaylist(val id: Long) : BottomSheetUiEvent
        data class AddSongToFavourite(val id: Long) : BottomSheetUiEvent
        data class RemoveSongToFavourite(val id: Long) : BottomSheetUiEvent
        data class ViewSongArtist(val id: Long) : BottomSheetUiEvent
    }
}