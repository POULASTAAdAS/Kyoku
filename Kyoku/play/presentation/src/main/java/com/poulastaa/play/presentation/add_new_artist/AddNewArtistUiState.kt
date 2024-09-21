package com.poulastaa.play.presentation.add_new_artist

import com.poulastaa.core.domain.model.ArtistPagingType
import com.poulastaa.core.presentation.ui.SnackBarUiState

data class AddNewArtistUiState(
    val isMakingApiCall: Boolean = false,
    val isSearchEnabled: Boolean = false,
    val searchQuery: String = "",
    val header: String = "",
    val isMassSelectEnabled: Boolean = false,

    val type: ArtistPagingType = ArtistPagingType.ALL,

    val toast: SnackBarUiState = SnackBarUiState(),
)

data class AddArtistUiArtist(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val isSelected: Boolean = false,
)
