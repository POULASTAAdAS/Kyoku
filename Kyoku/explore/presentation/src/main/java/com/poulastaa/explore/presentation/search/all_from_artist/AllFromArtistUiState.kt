package com.poulastaa.explore.presentation.search.all_from_artist

import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.TextHolder

internal data class AllFromArtistUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val filterType: FilterType = FilterType.ALL,

    val isSearchOpen: Boolean = false,
    val query: TextHolder = TextHolder(),
)

internal enum class FilterType {
    ALL,
    ALBUM,
    SONG
}

internal data class AllFromArtistUiItem(
    val id: Long = -1,
    val title: String = "",
    val poster: String = "",
    val artist: String = "",
    val isAlbum: Boolean = false,
)