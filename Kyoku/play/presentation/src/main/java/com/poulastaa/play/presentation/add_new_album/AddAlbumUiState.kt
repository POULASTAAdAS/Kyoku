package com.poulastaa.play.presentation.add_new_album

import com.poulastaa.core.domain.model.AlbumPagingType
import com.poulastaa.core.presentation.ui.SnackBarUiState

data class AddAlbumUiState(
    val isMakingApiCall: Boolean = false,

    val isSearchEnabled: Boolean = false,
    val searchQuery: String = "",
    val type: AlbumPagingType = AlbumPagingType.BY_POPULARITY,

    val isMassSelectEnabled: Boolean = false,

    val header: String = "",
    val threeDotOperations: List<AddAlbumOperation> = listOf(
        AddAlbumOperation.PLAY,
        AddAlbumOperation.SAVE_ALBUM

    ),

    val toast: SnackBarUiState = SnackBarUiState(),
)

data class AddAlbumUiAlbum(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val artist: String = "",
    val releaseYear: String = "",
    val isExtended: Boolean = false,
    val isSelected: Boolean = false,
)