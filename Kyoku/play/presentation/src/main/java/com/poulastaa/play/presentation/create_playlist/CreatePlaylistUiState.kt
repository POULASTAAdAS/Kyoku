package com.poulastaa.play.presentation.create_playlist

import com.poulastaa.core.domain.model.CreatePlaylistPagerFilterType
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.presentation.ui.model.UiSong
import com.poulastaa.play.domain.DataLoadingState

data class CreatePlaylistUiState(
    val playlistId: Long = -1,

    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val generatedData: List<CreatePlaylistData> = emptyList(),

    val header: String = "",

    val searchQuery: String = "",
    val isSearchEnabled: Boolean = false,

    val filterType: CreatePlaylistPagerFilterType = CreatePlaylistPagerFilterType.ALL,

    val savedSongIdList: List<Long> = emptyList(),

    val artistUiState: CreatePlaylistExpandedUiState = CreatePlaylistExpandedUiState(),
    val albumUiState: CreatePlaylistExpandedUiState = CreatePlaylistExpandedUiState(),
)

data class CreatePlaylistData(
    val type: CreatePlaylistType,
    val list: List<UiSong>,
)

data class CreatePlaylistPagingUiData(
    val id: Long,
    val title: String,
    val coverImage: String,
    val artist: String,
    val expandable: Boolean,
    val isArtist: Boolean,
)

data class CreatePlaylistExpandedUiState(
    val isExpanded: Boolean = false,
    val id: Long = -1,
)