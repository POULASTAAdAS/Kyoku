package com.poulastaa.add.presentation.playlist

import androidx.annotation.StringRes
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType

internal data class AddSongToPlaylistUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val query: String = "",
    val searchScreenFilterType: AddSongToPlaylistSearchUiFilterType = AddSongToPlaylistSearchUiFilterType.ALL,
    val staticData: List<AddSongToPlaylistPageUiItem> = listOf(
        AddSongToPlaylistPageUiItem(
            type = AddSongToPlaylistPageUiType.YOUR_FAVOURITES,
            data = (1..10).map {
                AddToPlaylistUiItem(
                    title = "That Cool Song",
                    artist = "That Cool Artist",
                    type = AddToPlaylistItemUiType.SONG
                )
            }
        ),
        AddSongToPlaylistPageUiItem(
            type = AddSongToPlaylistPageUiType.SUGGESTED_FOR_YOU,
            data = (1..10).map {
                AddToPlaylistUiItem(
                    title = "That Cool Song",
                    artist = "That Cool Artist",
                    type = AddToPlaylistItemUiType.SONG
                )
            }
        ),
        AddSongToPlaylistPageUiItem(
            type = AddSongToPlaylistPageUiType.YOU_MAY_ALSO_LIKE,
            data = (1..10).map {
                AddToPlaylistUiItem(
                    title = "That Cool Song",
                    artist = "That Cool Artist",
                    type = AddToPlaylistItemUiType.SONG
                )
            }
        )
    ),
)

internal data class AddToPlaylistUiItem(
    val id: Long = -1,
    val title: String = "",
    val poster: String? = null,
    val artist: String? = null,
    val numbers: Long = 0,
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