package com.poulastaa.setup.ui.import_spotify_playlist.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.common.ui.components.AppOutlinedTextField
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.components.ElevatedDefaultButton
import com.poulastaa.common.ui.components.ElevatedOutlinedButton
import com.poulastaa.common.ui.components.NetworkImage
import com.poulastaa.common.ui.components.RowSpacer
import com.poulastaa.common.ui.components.SmallClearButton
import com.poulastaa.common.ui.components.shimmerEffect
import com.poulastaa.common.ui.design_system.IconAddLink
import com.poulastaa.common.ui.design_system.IconArrowBack
import com.poulastaa.common.ui.design_system.StringAppName
import com.poulastaa.common.ui.design_system.StringBackButton
import com.poulastaa.common.ui.design_system.StringContinue
import com.poulastaa.common.ui.design_system.StringImportPlaylist
import com.poulastaa.common.ui.design_system.StringImportPlaylistTitle
import com.poulastaa.common.ui.design_system.StringPlaylistLink
import com.poulastaa.common.ui.design_system.StringSkipForNow
import com.poulastaa.common.ui.design_system.StringYourPlaylists
import com.poulastaa.common.ui.design_system.dimens
import com.poulastaa.common.ui.design_system.stringDurationHours
import com.poulastaa.common.ui.design_system.stringDurationMinutes
import com.poulastaa.common.ui.design_system.stringTracksCount
import com.poulastaa.setup.viewmodels.import_playlist.ImportPlaylistUiAction
import com.poulastaa.setup.viewmodels.import_playlist.ImportPlaylistUiState
import com.poulastaa.setup.viewmodels.import_playlist.ImportPlaylistViewmodel
import com.poulastaa.setup.viewmodels.import_playlist.UiDuration

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
        Text(
            style = MaterialTheme.typography.headlineSmall,
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
                ) {
                    append(StringImportPlaylistTitle)
                }
                withStyle(
                    style = SpanStyle(color = MaterialTheme.colorScheme.primary)
                ) {
                    append(StringAppName)
                }
            },
        )

        ColumnSpacer(MaterialTheme.dimens.spacing.large)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            // OutlinedTextField reserves top space for its label, align bottoms so the button matches the field outline
            verticalAlignment = Alignment.Bottom
        ) {
            AppOutlinedTextField(
                value = state.link.value,
                onValueChange = { viewmodel.onAction(ImportPlaylistUiAction.OnLinkChange(it)) },
                modifier = Modifier.weight(1f),
                label = StringPlaylistLink,
                leadingIcon = IconAddLink,
                trailingContent = {
                    SmallClearButton(
                        visible = state.link.value.isNotEmpty(),
                        oClick = { viewmodel.onAction(ImportPlaylistUiAction.OnLinkChange("")) }
                    )
                },
                isError = state.link.isError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewmodel.onAction(ImportPlaylistUiAction.OnImportClick)
                    }
                ),
            )

            RowSpacer(MaterialTheme.dimens.spacing.medium)

            Box(Modifier.fillMaxWidth(0.3f)) {
                ElevatedDefaultButton(
                    width = 1f,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        focusManager.clearFocus()
                        viewmodel.onAction(ImportPlaylistUiAction.OnImportClick)
                    },
                    content = {
                        AnimatedContent(
                            targetState = state.isMakingApiCall,
                            modifier = Modifier.fillMaxWidth()
                                .height(TextFieldDefaults.MinHeight),
                        ) {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                when (it) {
                                    true -> CircularProgressIndicator(
                                        modifier = Modifier.fillMaxWidth(0.25f).aspectRatio(1f),
                                        color = MaterialTheme.colorScheme.background
                                    )

                                    false -> Text(
                                        text = StringImportPlaylist,
                                        color = MaterialTheme.colorScheme.background,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }

        ColumnSpacer(MaterialTheme.dimens.spacing.large)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                Modifier
                    .weight(1f)
                    .height(0.5.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.onSurfaceVariant,
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        )
                    )
            )

            RowSpacer(MaterialTheme.dimens.spacing.small)

            Text(
                text = StringYourPlaylists,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp
                )
            )

            RowSpacer(MaterialTheme.dimens.spacing.small)

            Spacer(
                Modifier
                    .weight(1f)
                    .height(0.5.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.onSurfaceVariant,
                                MaterialTheme.colorScheme.onSurfaceVariant,
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        ColumnSpacer(MaterialTheme.dimens.spacing.large)

        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                if (state.isLoading) items(LOADING_PLAYLIST_COUNT) {
                    LoadingPlaylistItem()
                }

                if (!state.isLoading) items(
                    items = state.playlists,
                    key = { it.id }
                ) { playlist ->
                    Card(
                        modifier = Modifier.fillMaxWidth().animateContentSize(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        viewmodel.onAction(ImportPlaylistUiAction.OnPlaylistToggle(playlist.id))
                                    }
                                )
                                .padding(MaterialTheme.dimens.spacing.small)
                                .height(56.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = Modifier.fillMaxHeight()
                                    .aspectRatio(1f),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 3.dp
                                ),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                val images = playlist.posters

                                if (images.size >= 4) Column(
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.medium)
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Card(
                                            modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(),
                                            shape = RectangleShape,
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            ),
                                        ) {
                                            NetworkImage(
                                                imageUrl = images[0],
                                                modifier = Modifier.fillMaxSize(),
                                                placeholderFraction = 0.8f,
                                            )
                                        }

                                        Card(
                                            shape = RectangleShape,
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            ),
                                        ) {
                                            NetworkImage(
                                                imageUrl = images[1],
                                                modifier = Modifier.fillMaxSize(),
                                                placeholderFraction = 0.8f,
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Card(
                                            modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(),
                                            shape = RectangleShape,
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            ),
                                        ) {
                                            NetworkImage(
                                                imageUrl = images[2],
                                                modifier = Modifier.fillMaxSize(),
                                                placeholderFraction = 0.8f,
                                            )
                                        }

                                        Card(
                                            shape = RectangleShape,
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            ),
                                        ) {
                                            NetworkImage(
                                                imageUrl = images[3],
                                                modifier = Modifier.fillMaxSize(),
                                                placeholderFraction = 0.8f,
                                            )
                                        }
                                    }
                                } else Card(
                                    modifier = Modifier.fillMaxSize(),
                                    shape = RectangleShape,
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                ) {
                                    NetworkImage(
                                        imageUrl = images.firstOrNull(),
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }
                            }

                            RowSpacer(MaterialTheme.dimens.spacing.medium)

                            Column(
                                modifier = Modifier.weight(1f).fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = playlist.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringTracksCount(playlist.tracks.size),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.7f
                                        ),
                                    )

                                    playlist.totalDuration?.let { totalDuration ->
                                        Box(
                                            Modifier.padding(MaterialTheme.dimens.spacing.extraSmall),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                Modifier.clip(CircleShape)
                                                    .size(MaterialTheme.dimens.spacing.extraSmall)
                                                    .background(
                                                        color = MaterialTheme.colorScheme.primary,
                                                        shape = CircleShape
                                                    )
                                            )
                                        }

                                        Text(
                                            text = when (totalDuration.unit) {
                                                UiDuration.UiTimeUnit.HOURS -> stringDurationHours(totalDuration.time)
                                                UiDuration.UiTimeUnit.MINUTES -> stringDurationMinutes(totalDuration.time)
                                            },
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                alpha = 0.7f
                                            ),
                                        )
                                    }
                                }
                            }

                            IconButton(
                                onClick = {
                                    viewmodel.onAction(ImportPlaylistUiAction.OnPlaylistToggle(playlist.id))
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                            ) {
                                Box(
                                    Modifier
                                        .rotate(if (playlist.isExpanded) 270f else 90f)
                                        .padding(end = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = IconArrowBack,
                                        contentDescription = StringBackButton,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = playlist.isExpanded,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val isVisible by remember { mutableStateOf(playlist.isExpanded) }

                            if (isVisible) Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Spacer(
                                    Modifier.fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.dimens.spacing.large)
                                        .height(0.4.dp)
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                listOf(
                                                    Color.Transparent,
                                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                                    Color.Transparent
                                                )
                                            )
                                        )
                                )

                                ColumnSpacer(MaterialTheme.dimens.spacing.small)

                                playlist.tracks.forEach { song ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(horizontal = MaterialTheme.dimens.spacing.large)
                                            .heightIn(max = 48.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Card(
                                            modifier = Modifier.fillMaxHeight()
                                                .aspectRatio(1f),
                                            shape = MaterialTheme.shapes.medium,
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            )
                                        ) {
                                            NetworkImage(
                                                imageUrl = song.poster,
                                                modifier = Modifier.fillMaxSize(),
                                                contentDescription = song.title,
                                            )
                                        }

                                        RowSpacer(MaterialTheme.dimens.spacing.small)

                                        Column(
                                            modifier = Modifier.weight(1f).fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Text(
                                                text = song.title,
                                                style = MaterialTheme.typography.bodyMedium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Text(
                                                text = song.artist.joinToString(", "),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize.value.minus(
                                                        4
                                                    ).sp
                                                ),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                    alpha = 0.8f
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        song.duration?.let { duration ->
                                            Text(
                                                text = duration.time,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                    alpha = 0.7f
                                                ),
                                            )
                                        }
                                    }

                                    ColumnSpacer(MaterialTheme.dimens.spacing.small)
                                }
                            }
                        }
                    }

                    ColumnSpacer(MaterialTheme.dimens.spacing.small)
                }

                // the playlist being imported lands right after the existing ones (top when empty)
                if (!state.isLoading && state.isMakingApiCall) item(key = IMPORTING_PLAYLIST_KEY) {
                    LoadingPlaylistItem(Modifier.animateItem())
                }

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
                ElevatedOutlinedButton(
                    shape = MaterialTheme.shapes.large,
                    content = {
                        Box(
                            Modifier.fillMaxWidth()
                                .minimumInteractiveComponentSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedContent(
                                targetState = state.playlists.isNotEmpty(),
                            ) { status ->
                                when (status) {
                                    false -> Text(text = StringSkipForNow)
                                    true -> Text(text = StringContinue)
                                }
                            }
                        }
                    },
                    onClick = onContinueClick
                )

                Spacer(
                    Modifier.navigationBarsPadding()
                        .padding(MaterialTheme.dimens.spacing.medium)
                )
            }
        }
    }
}

private const val LOADING_PLAYLIST_COUNT = 4
private const val IMPORTING_PLAYLIST_KEY = "importing_playlist"

@Composable
private fun LoadingPlaylistItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(vertical = MaterialTheme.dimens.spacing.extraSmall)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(MaterialTheme.dimens.spacing.small)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.fillMaxHeight()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
                .shimmerEffect()
        )

        RowSpacer(MaterialTheme.dimens.spacing.medium)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                Modifier.fillMaxWidth(0.6f)
                    .height(14.dp)
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect()
            )

            ColumnSpacer(MaterialTheme.dimens.spacing.small)

            Box(
                Modifier.fillMaxWidth(0.3f)
                    .height(10.dp)
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect()
            )
        }
    }
}
