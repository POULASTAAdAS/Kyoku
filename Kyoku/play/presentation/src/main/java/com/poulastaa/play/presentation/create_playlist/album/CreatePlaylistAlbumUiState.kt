package com.poulastaa.play.presentation.create_playlist.album

import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistPagingUiData
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevAlbum

data class CreatePlaylistAlbumUiState(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val header: String = "",
    val album: UiPrevAlbum = UiPrevAlbum(),
    val albumSongs: List<CreatePlaylistPagingUiData> = emptyList(),
)
