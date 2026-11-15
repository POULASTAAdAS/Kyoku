package com.poulastaa.setup.ui.import_spotify_playlist.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
internal fun CompatVerticalImportSpotifyPlaylistScreen(
    focusManager: FocusManager,
    state: ImportPlaylistUiState,
    viewmodel: ImportPlaylistViewmodel,
    onContinueClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = MaterialTheme.dimens.layout.contentPadding)
            .padding(top = MaterialTheme.dimens.layout.contentPadding)
            .systemBarsPadding(),
    ) {
        ImportPlaylistTitle()

        ColumnSpacer(MaterialTheme.dimens.spacing.large)

        ImportPlaylistLinkField(state, viewmodel, focusManager)

        ColumnSpacer(MaterialTheme.dimens.spacing.large)

        YourPlaylistsDivider()

        ColumnSpacer(MaterialTheme.dimens.spacing.large)

        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                importPlaylistItems(state, viewmodel)

                item {
                    Spacer(
                        Modifier
                            .navigationBarsPadding()
                            .padding(MaterialTheme.dimens.spacing.large)
                    )
                }

                item {
                    Spacer(
                        Modifier
                            .minimumInteractiveComponentSize()
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ImportPlaylistContinueButton(state, onContinueClick)

                Spacer(
                    Modifier.navigationBarsPadding()
                        .padding(MaterialTheme.dimens.spacing.medium)
                )
            }
        }
    }
}
