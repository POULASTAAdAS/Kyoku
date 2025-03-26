package com.poulastaa.explore.presentation.search.all_from_artist

import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.SongId

internal sealed interface AllFromArtistUiAction {
    data object OnToggleSearch : AllFromArtistUiAction
    data class OnSearchQueryChange(val query: String) : AllFromArtistUiAction
    data object OnSearchQueryClear : AllFromArtistUiAction
    data class OnFilterChange(val filterType: FilterType) : AllFromArtistUiAction

    data class OnSongClick(val songId: SongId) : AllFromArtistUiAction
    data class OnAlbumClick(val albumId: AlbumId) : AllFromArtistUiAction
}