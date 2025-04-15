package com.poulastaa.add.presentation.playlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poulastaa.add.presentation.playlist.AddSongToPlaylistSearchUiFilterType
import com.poulastaa.add.presentation.playlist.AddSongToPlaylistUiAction
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppFilterChip

@Composable
internal fun AddSongToPlaylistSearchFilterChips(
    modifier: Modifier = Modifier,
    filterType: AddSongToPlaylistSearchUiFilterType,
    onAction: (AddSongToPlaylistUiAction.OnSearchFilterTypeChange) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppFilterChip(
            title = stringResource(AddSongToPlaylistSearchUiFilterType.ALL.value),
            isSelected = filterType == AddSongToPlaylistSearchUiFilterType.ALL,
            onClick = {
                onAction(
                    AddSongToPlaylistUiAction.OnSearchFilterTypeChange(
                        AddSongToPlaylistSearchUiFilterType.ALL
                    )
                )
            }
        )

        AppFilterChip(
            title = stringResource(AddSongToPlaylistSearchUiFilterType.SONG.value),
            isSelected = filterType == AddSongToPlaylistSearchUiFilterType.SONG,
            onClick = {
                onAction(
                    AddSongToPlaylistUiAction.OnSearchFilterTypeChange(
                        AddSongToPlaylistSearchUiFilterType.SONG
                    )
                )
            }
        )

        AppFilterChip(
            title = stringResource(AddSongToPlaylistSearchUiFilterType.ALBUM.value),
            isSelected = filterType == AddSongToPlaylistSearchUiFilterType.ALBUM,
            onClick = {
                onAction(
                    AddSongToPlaylistUiAction.OnSearchFilterTypeChange(
                        AddSongToPlaylistSearchUiFilterType.ALBUM
                    )
                )
            }
        )

        AppFilterChip(
            title = stringResource(AddSongToPlaylistSearchUiFilterType.ARTIST.value),
            isSelected = filterType == AddSongToPlaylistSearchUiFilterType.ARTIST,
            onClick = {
                onAction(
                    AddSongToPlaylistUiAction.OnSearchFilterTypeChange(
                        AddSongToPlaylistSearchUiFilterType.ARTIST
                    )
                )
            }
        )

        AppFilterChip(
            title = stringResource(AddSongToPlaylistSearchUiFilterType.PLAYLIST.value),
            isSelected = filterType == AddSongToPlaylistSearchUiFilterType.PLAYLIST,
            onClick = {
                onAction(
                    AddSongToPlaylistUiAction.OnSearchFilterTypeChange(
                        AddSongToPlaylistSearchUiFilterType.PLAYLIST
                    )
                )
            }
        )
    }
}