package com.poulastaa.add.presentation.playlist.album

import com.poulastaa.add.presentation.playlist.AddSongToPlaylistUiItem
import com.poulastaa.core.presentation.designsystem.model.LoadingType

internal data class AddSongToPlaylistAlbumUiState(
    val loadingSate: LoadingType = LoadingType.Loading,
    val album: String = "",
    val data: List<AddSongToPlaylistUiItem> = emptyList()
)
