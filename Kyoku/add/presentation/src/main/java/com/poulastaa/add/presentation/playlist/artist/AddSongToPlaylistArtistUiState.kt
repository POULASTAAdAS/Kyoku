package com.poulastaa.add.presentation.playlist.artist

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.TextHolder
import com.poulastaa.core.presentation.designsystem.model.UiPrevArtist

internal data class AddSongToPlaylistArtistUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val filterType: AddSongToPlaylistArtistSearchFilterType = AddSongToPlaylistArtistSearchFilterType.ALL,
    val query: TextHolder = TextHolder(),
    val artist: UiPrevArtist = UiPrevArtist(),
    val playlistId: PlaylistId = -1,
)

internal enum class AddSongToPlaylistArtistSearchFilterType(
    @StringRes val text: Int,
    @DrawableRes val icon: Int,
) {
    ALL(
        text = R.string.all,
        icon = R.drawable.ic_filter_all
    ),
    ALBUM(
        text = R.string.album,
        icon = R.drawable.ic_filter_album
    ),
    SONG(
        text = R.string.song,
        icon = R.drawable.ic_music_vector
    )
}
