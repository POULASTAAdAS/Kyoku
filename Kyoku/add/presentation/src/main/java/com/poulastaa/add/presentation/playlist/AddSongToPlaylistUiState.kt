package com.poulastaa.add.presentation.playlist

import androidx.annotation.StringRes
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType

internal data class AddSongToPlaylistUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val favourite: List<AddToPlaylistUiItem> = emptyList(),
    val query: String = "",
    val isSearchEnabled: Boolean = false,
    val searchScreenFilterType: AddSongToPlaylistSearchUiFilterType = AddSongToPlaylistSearchUiFilterType.ALL,
    val staticData: List<AddSongToPlaylistPageUiItem> = emptyList(),
)

internal data class AddToPlaylistUiItem(
    val id: Long = -1,
    val title: String = "",
    val poster: String? = null,
    val type: AddToPlaylistItemUiType,
)

internal enum class AddToPlaylistItemUiType {
    PLAYLIST,
    ALBUM,
    ARTIST,
    SONG
}

internal enum class AddSongToPlaylistSearchUiFilterType(@StringRes val value: Int) {
    ALL(R.string.all),
    ALBUM(R.string.album),
    SONG(R.string.song),
    ARTIST(R.string.artist),
    PLAYLIST(R.string.playlist)
}

internal data class AddSongToPlaylistPageUiItem(
    val type: AddSongToPlaylistPageUiType = AddSongToPlaylistPageUiType.YOUR_FAVOURITES,
    val data: List<AddToPlaylistUiItem> = emptyList(),
)

internal enum class AddSongToPlaylistPageUiType(@StringRes val value: Int) {
    YOUR_FAVOURITES(R.string.your_favourite),
    SUGGESTED_FOR_YOU(R.string.suggested_for_you),
    YOU_MAY_ALSO_LIKE(R.string.you_may_also_like),
}