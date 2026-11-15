package com.poulastaa.setup.ui.import_spotify_playlist.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.design_system.dimens
import com.poulastaa.setup.ui.import_spotify_playlist.components.ImportPlaylistContinueButton
import com.poulastaa.setup.ui.import_spotify_playlist.components.ImportPlaylistLinkField
import com.poulastaa.setup.ui.import_spotify_playlist.components.ImportPlaylistTitle
import com.poulastaa.setup.ui.import_spotify_playlist.components.YourPlaylistsDivider
import com.poulastaa.setup.ui.import_spotify_playlist.components.importPlaylistItems
import com.poulastaa.setup.viewmodels.import_playlist.ImportPlaylistUiState
import com.poulastaa.setup.viewmodels.import_playlist.ImportPlaylistViewmodel

@Composable
internal fun CompatHorizontalImportSpotifyPlaylistScreen(
    focusManager: FocusManager,
    state: ImportPlaylistUiState,
    viewmodel: ImportPlaylistViewmodel,
    onContinueClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(MaterialTheme.dimens.layout.contentPadding),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.layout.paneSpacing),
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
        ) {
            ImportPlaylistTitle()

            ColumnSpacer(MaterialTheme.dimens.spacing.large)

            ImportPlaylistLinkField(state, viewmodel, focusManager)

            Spacer(Modifier.weight(1f))

            ImportPlaylistContinueButton(state, onContinueClick)
        }

        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
        ) {
            YourPlaylistsDivider()

            ColumnSpacer(MaterialTheme.dimens.spacing.large)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                importPlaylistItems(state, viewmodel)

                item {
                    Spacer(Modifier.navigationBarsPadding())
                }
            }
        }
    }
}
