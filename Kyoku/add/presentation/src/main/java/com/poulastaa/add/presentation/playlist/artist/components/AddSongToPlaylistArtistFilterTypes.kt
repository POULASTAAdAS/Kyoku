package com.poulastaa.add.presentation.playlist.artist.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.poulastaa.add.presentation.playlist.artist.AddSongToPlaylistArtistSearchFilterType
import com.poulastaa.add.presentation.playlist.artist.AddSongToPlaylistArtistUiAction
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppFilterChip

@Composable
internal fun AddSongToPlaylistArtistFilterTypes(
    filterType: AddSongToPlaylistArtistSearchFilterType,
    onAction: (AddSongToPlaylistArtistUiAction.OnSearchFilterTypeChange) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(tween(400)),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppFilterChip(
            title = stringResource(AddSongToPlaylistArtistSearchFilterType.ALL.text),
            icon = ImageVector.vectorResource(
                AddSongToPlaylistArtistSearchFilterType.ALL.icon
            ),
            isSelected = filterType == AddSongToPlaylistArtistSearchFilterType.ALL,
            onClick = {
                onAction(
                    AddSongToPlaylistArtistUiAction.OnSearchFilterTypeChange(
                        AddSongToPlaylistArtistSearchFilterType.ALL
                    )
                )
            }
        )

        AppFilterChip(
            title = stringResource(AddSongToPlaylistArtistSearchFilterType.SONG.text),
            icon = ImageVector.vectorResource(
                AddSongToPlaylistArtistSearchFilterType.SONG.icon
            ),
            isSelected = filterType == AddSongToPlaylistArtistSearchFilterType.SONG,
            onClick = {
                onAction(
                    AddSongToPlaylistArtistUiAction.OnSearchFilterTypeChange(
                        AddSongToPlaylistArtistSearchFilterType.SONG
                    )
                )
            }
        )

        AppFilterChip(
            title = stringResource(AddSongToPlaylistArtistSearchFilterType.ALBUM.text),
            icon = ImageVector.vectorResource(
                AddSongToPlaylistArtistSearchFilterType.ALBUM.icon
            ),
            isSelected = filterType == AddSongToPlaylistArtistSearchFilterType.ALBUM,
            onClick = {
                onAction(
                    AddSongToPlaylistArtistUiAction.OnSearchFilterTypeChange(
                        AddSongToPlaylistArtistSearchFilterType.ALBUM
                    )
                )
            }
        )
    }
}