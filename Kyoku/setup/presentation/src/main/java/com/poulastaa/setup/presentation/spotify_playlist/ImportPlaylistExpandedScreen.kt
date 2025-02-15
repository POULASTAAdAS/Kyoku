package com.poulastaa.setup.presentation.spotify_playlist

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.LinkIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.components.AppLoadingButton
import com.poulastaa.core.presentation.ui.components.AppTextField
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.designsystem.ui.gradiantBackground
import com.poulastaa.setup.presentation.spotify_playlist.components.SongList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportPlaylistExpandedScreen(
    state: ImportPlaylistUiState,
    onAction: (ImportPlaylistUiAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    Scaffold(
        floatingActionButton = {
            AppLoadingButton(
                modifier = Modifier.fillMaxWidth(.23f),
                text = stringResource(state.floatActionButtonText),
                fontWeight = FontWeight.Black,
                onClick = {
                    onAction(ImportPlaylistUiAction.OnSkipClick)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.import_playlist),
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradiantBackground()
                    )
                )
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.dimens.medium1)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.4f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AppTextField(
                    modifier = Modifier.fillMaxWidth(),
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

                Spacer(Modifier.height(MaterialTheme.dimens.small3))

                AppLoadingButton(
                    modifier = Modifier.fillMaxWidth(.5f),
                    text = stringResource(R.string.import_label),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    onClick = {
                        onAction(ImportPlaylistUiAction.OnImportClick)
                        focusManager.clearFocus()
                    },
                    isLoading = state.isMakingApiCall,
                    loadingColor = MaterialTheme.colorScheme.background,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                )
            }

            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

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
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                gradiantBackground()
                            )
                        ),
                    contentPadding = PaddingValues(MaterialTheme.dimens.small2),
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
                            onPlaylistClick = {
                                onAction(ImportPlaylistUiAction.OnPlaylistClick(it))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 840,
    heightDp = 420
)
@Preview(
    widthDp = 840,
    heightDp = 420
)
@Composable
private fun Preview() {
    AppThem {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            ImportPlaylistExpandedScreen(
                state = ImportPlaylistUiState(),
                onAction = {},
            )
        }
    }
}