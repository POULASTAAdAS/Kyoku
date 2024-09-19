package com.poulastaa.play.presentation.song_artist

import com.poulastaa.play.domain.DataLoadingState

data class SongArtistsUiState(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val header: String = "",
    val artist: List<SongArtistUiArtist> = emptyList(),
)

data class SongArtistUiArtist(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val popularity: Long = 0,
)