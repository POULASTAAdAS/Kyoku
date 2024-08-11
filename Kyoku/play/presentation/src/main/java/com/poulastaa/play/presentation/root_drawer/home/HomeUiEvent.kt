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

    sealed interface ItemBottomSheetUiEvent : HomeUiEvent {
        data object Cancel : ItemBottomSheetUiEvent

        data object PlayPopularSongMix : ItemBottomSheetUiEvent
        data object PlayOldGem : ItemBottomSheetUiEvent
        data object PlayFavouriteArtistMix : ItemBottomSheetUiEvent

        data object AddAsPlaylistPopularSongMix : ItemBottomSheetUiEvent
        data object AddAsPlaylistOldGem : ItemBottomSheetUiEvent
        data object AddAsFavouriteArtistMix : ItemBottomSheetUiEvent

        data object DownloadPopularSongMix : ItemBottomSheetUiEvent
        data object DownloadOldGem : ItemBottomSheetUiEvent
        data object DownloadFavouriteArtistMix : ItemBottomSheetUiEvent

        data class FollowArtist(val id: Long) : ItemBottomSheetUiEvent
        data class UnFollowArtist(val id: Long) : ItemBottomSheetUiEvent
        data class ExploreArtist(val id: Long) : ItemBottomSheetUiEvent

        data class PlayAlbum(val id: Long) : ItemBottomSheetUiEvent
        data class SaveAlbum(val id: Long) : ItemBottomSheetUiEvent
        data class RemoveSavedAlbum(val id: Long) : ItemBottomSheetUiEvent

        data class PlaySong(val id: Long) : ItemBottomSheetUiEvent
        data class RemoveFromQueue(val id: Long) : ItemBottomSheetUiEvent
        data class PlayNextOnQueue(val id: Long) : ItemBottomSheetUiEvent
        data class PlayLastOnQueue(val id: Long) : ItemBottomSheetUiEvent
        data class AddSongToPlaylist(
            val id: Long,
            val artistId: Long?,
        ) : ItemBottomSheetUiEvent

        data class AddSongToFavourite(val id: Long) : ItemBottomSheetUiEvent
        data class RemoveSongToFavourite(val id: Long) : ItemBottomSheetUiEvent
        data class ViewSongArtist(val id: Long) : ItemBottomSheetUiEvent
    }

    sealed interface PlaylistBottomSheetUiEvent : HomeUiEvent {
        data object Cancel : PlaylistBottomSheetUiEvent
    }
}