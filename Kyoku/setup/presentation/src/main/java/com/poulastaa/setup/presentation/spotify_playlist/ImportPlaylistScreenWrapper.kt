package com.poulastaa.setup.presentation.spotify_playlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.ui.LinkIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppLoadingButton
import com.poulastaa.core.presentation.ui.components.AppTextField
import com.poulastaa.setup.presentation.spotify_playlist.components.SongList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportPlaylistScreenWrapper(
    modifier: Modifier,
    state: ImportPlaylistUiState,
    floatingActionButtonWidth: Float = .65f,
    floatingActionButtonText: Int,
    floatingButtonPos: FabPosition = FabPosition.Center,
    contentPadding: Dp,
    contentSpacePadding: Dp,
    onFloatingActionButtonClick: () -> Unit,
    onLinkChange: (String) -> Unit,
    onImportClick: () -> Unit,
    onPlaylistClick: (id: Long) -> Unit,
) {
    Scaffold(
        topBar = {
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
        },
        floatingActionButtonPosition = floatingButtonPos,
        floatingActionButton = {
            AppLoadingButton(
                modifier = Modifier.fillMaxWidth(floatingActionButtonWidth),
                text = stringResource(floatingActionButtonText),
                fontWeight = FontWeight.Black,
                onClick = onFloatingActionButtonClick,
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
                .background(
                    brush = Brush.verticalGradient(
                        colors = ThemChanger.getGradiantBackground()
                    )
                )
                .padding(paddingValues)
                .padding(MaterialTheme.dimens.medium1),
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    AppTextField(
                        modifier = modifier,
                        text = state.link.value,
                        onValueChange = onLinkChange,
                        label = stringResource(R.string.link),
                        trailingIcon = LinkIcon,
                        supportingText = state.link.errText.asString(),
                        isError = state.link.isErr,
                        imeAction = ImeAction.Go,
                        keyboardActions = KeyboardActions(
                            onGo = {
                                onImportClick()
                            }
                        )
                    )

                    Spacer(Modifier.width(MaterialTheme.dimens.medium1))
                    Spacer(Modifier.weight(1f))

                    AppLoadingButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.import_label),
                        fontWeight = FontWeight.SemiBold,
                        onClick = onImportClick,
                        isLoading = state.isMakingApiCall,
                        loadingColor = MaterialTheme.colorScheme.background,
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
                            .background(
                                brush = Brush.verticalGradient(
                                    ThemChanger.getGradiantBackground()
                                )
                            )
                            .fillMaxSize(),
                        contentPadding = PaddingValues(contentPadding),
                        verticalArrangement = Arrangement.spacedBy(contentSpacePadding)
                    ) {
                        items(
                            items = state.data,
                            key = {
                                it.playlist.id
                            }
                        ) { item ->
                            SongList(
                                item = item,
                                onPlaylistClick = onPlaylistClick,
                            )
                        }
                    }
                }
            }
        )
    }
}