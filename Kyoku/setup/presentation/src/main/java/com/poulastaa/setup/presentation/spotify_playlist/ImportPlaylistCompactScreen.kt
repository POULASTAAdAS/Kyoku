package com.poulastaa.setup.presentation.spotify_playlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.LinkIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppLoadingButton
import com.poulastaa.core.presentation.designsystem.components.AppTextField
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.setup.presentation.spotify_playlist.components.SongList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ImportPlaylistCompactScreen(
    state: ImportPlaylistUiState,
    onAction: (ImportPlaylistUiAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.import_playlist),
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            AppLoadingButton(
                modifier = Modifier
                    .fillMaxWidth(.65f)
                    .height(56.dp),
                text = stringResource(state.floatActionButtonText),
                fontWeight = FontWeight.Black,
                onClick = { onAction(ImportPlaylistUiAction.OnSkipClick) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(MaterialTheme.dimens.medium1)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(74.dp)
            ) {
                AppTextField(
                    modifier = Modifier.fillMaxWidth(.65f),
                    text = state.link.value,
                    onValueChange = { onAction(ImportPlaylistUiAction.OnLinkChange(it)) },
                    label = stringResource(R.string.link),
                    trailingIcon = LinkIcon,
                    supportingText = state.link.errText.asString(),
                    isError = state.link.isErr,
                    imeAction = ImeAction.Go,
                    keyboardActions = KeyboardActions(
                        onGo = {
                            onAction(ImportPlaylistUiAction.OnImportClick)
                            focusManager.clearFocus()
                        }
                    )
                )

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                AppLoadingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    text = stringResource(R.string.import_label),
                    fontWeight = FontWeight.SemiBold,
                    onClick = {
                        onAction(ImportPlaylistUiAction.OnImportClick)
                        focusManager.clearFocus()
                    },
                    isLoading = state.isMakingApiCall,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = MaterialTheme.dimens.medium1),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(MaterialTheme.dimens.small3),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
                ) {
                    items(
                        state.data,
                        key = {
                            it.playlist.id
                        }
                    ) { item ->
                        SongList(
                            item = item,
                            haptic = haptic,
                            onAction = onAction,
                        )
                    }
                }
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ImportPlaylistCompactScreen(
                state = ImportPlaylistUiState(),
                onAction = {}
            )
        }
    }
}