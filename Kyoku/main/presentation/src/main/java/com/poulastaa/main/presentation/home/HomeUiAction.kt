package com.poulastaa.main.presentation.home

import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.main.presentation.components.UiMainViewMoreItemType
import com.poulastaa.main.presentation.components.UiSaveItemType

internal sealed interface HomeUiAction {
    data class OnSavedItemCLick(
        val id: Long,
        val type: UiSaveItemType?,
        val clickType: ItemClickType,
    ) : HomeUiAction

    data class OnExploreTypeItemClick(
        val type: UiHomeExploreType,
        val clickType: ItemClickType,
    ) : HomeUiAction

    data class OnSuggestedArtistLongClick(val id: ArtistId) : HomeUiAction
    data class OnSuggestedAlbumLongClick(val id: AlbumId) : HomeUiAction

    data class OnViewMore(
        val type: UiMainViewMoreItemType,
        val id: Long = -1,
    ) : HomeUiAction

    data class OnSuggestArtistSongClick(
        val artistId: ArtistId,
        val songId: SongId,
        val clickType: ItemClickType,
    ) : HomeUiAction
}