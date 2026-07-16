package com.poulastaa.setup.ui.import_spotify_playlist.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.components.AppOutlinedTextField
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.components.ElevatedDefaultButton
import com.poulastaa.common.ui.components.ElevatedOutlinedButton
import com.poulastaa.common.ui.components.RowSpacer
import com.poulastaa.common.ui.components.SmallClearButton
import com.poulastaa.common.ui.design_system.IconAddLink
import com.poulastaa.common.ui.design_system.IconAppLogo
import com.poulastaa.common.ui.design_system.IconArrowBack
import com.poulastaa.common.ui.design_system.StringBackButton
import com.poulastaa.common.ui.design_system.dimens
import com.poulastaa.setup.ui.import_spotify_playlist.UiPlaylist

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun CompatVerticalImportSpotifyPlaylistScreen(
    focusManager: FocusManager,
    isLoading: Boolean,
    onLoadingChange: (Boolean) -> Unit,
    link: String,
    onLinkChange: (String) -> Unit,
    navController: NavHostController,
    playlists: List<UiPlaylist>,
    onPlaylistsChange: (List<UiPlaylist>) -> Unit,
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
                    append("Import your spotify playlist to ")
                }
                withStyle(
                    style = SpanStyle(color = MaterialTheme.colorScheme.primary)
                ) {
                    append("Kyoku")
                }
            },
        )

        ColumnSpacer(MaterialTheme.dimens.spacing.large)

        Row(
            modifier = Modifier.fillMaxWidth()
                .heightIn(max = 64.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppOutlinedTextField(
                value = link,
                onValueChange = onLinkChange,
                modifier = Modifier.weight(1f),
                label = "playlist link",
                leadingIcon = IconAddLink,
                trailingContent = {
                    SmallClearButton(
                        visible = link.isNotEmpty(),
                        oClick = { onLinkChange("") }
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onLoadingChange(true)
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
                        if (isLoading.not()) {
                            onLoadingChange(true)
                            // api call
                        }
                    },
                    content = {
                        AnimatedContent(
                            targetState = isLoading,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Box(
                                Modifier.fillMaxSize(0.8f),
                                contentAlignment = Alignment.Center
                            ) {
                                when (it) {
                                    true -> CircularProgressIndicator(
                                        modifier = Modifier.fillMaxWidth(0.25f).aspectRatio(1f),
                                        color = MaterialTheme.colorScheme.background
                                    )

                                    false -> Text(
                                        text = "Import",
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
                text = "YOUR PLAYLISTS",
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
                items(
                    items = playlists,
                    key = { it.id }
                ) { playlist ->
                    Card(
                        modifier = Modifier.fillMaxWidth().animateContentSize(),
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        onPlaylistsChange(
                                            playlists.map {
                                                if (it.id == playlist.id) it.copy(isExpanded = !it.isExpanded)
                                                else it
                                            }
                                        )
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
                                shape = MaterialTheme.shapes.small
                            ) {
                                val images = playlist.posters

                                if (images.size >= 4) Column(
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.small)
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
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            ),
                                        ) {
                                            Box(
                                                Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = IconAppLogo,
                                                    modifier = Modifier.fillMaxSize(0.8f),
                                                    contentDescription = null
                                                )
                                            }
                                        }

                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            ),
                                        ) {
                                            Box(
                                                Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = IconAppLogo,
                                                    modifier = Modifier.fillMaxSize(0.8f),
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Card(
                                            modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            ),
                                        ) {
                                            Box(
                                                Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = IconAppLogo,
                                                    modifier = Modifier.fillMaxSize(0.8f),
                                                    contentDescription = null
                                                )
                                            }
                                        }

                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            ),
                                        ) {
                                            Box(
                                                Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = IconAppLogo,
                                                    modifier = Modifier.fillMaxSize(0.8f),
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                } else Card(
                                    modifier = Modifier.fillMaxSize(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                ) {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = IconAppLogo,
                                            modifier = Modifier.fillMaxSize(0.6f),
                                            contentDescription = null
                                        )
                                    }
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
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${playlist.tracks.size} tracks",
                                        style = MaterialTheme.typography.bodySmall
                                    )

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
                                        text = "${playlist.totalDuration.time} ${playlist.totalDuration.unit.value}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    onPlaylistsChange(
                                        playlists.map {
                                            if (it.id == playlist.id) it.copy(isExpanded = !it.isExpanded)
                                            else it
                                        }
                                    )
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
                                            shape = MaterialTheme.shapes.small,
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            )
                                        ) {
                                            Box(
                                                Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = IconAppLogo,
                                                    contentDescription = song.title,
                                                    modifier = Modifier.fillMaxSize(0.6f)
                                                )
                                            }
                                        }

                                        RowSpacer(MaterialTheme.dimens.spacing.small)

                                        Column(
                                            modifier = Modifier.weight(1f).fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Text(
                                                text = song.title,
                                                style = MaterialTheme.typography.bodySmall,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Text(
                                                text = song.artist.joinToString(", "),
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = MaterialTheme.typography.bodySmall.fontSize.value.minus(
                                                        4
                                                    ).sp
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        Text(
                                            text = song.duration.time,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    ColumnSpacer(MaterialTheme.dimens.spacing.small)
                                }
                            }
                        }
                    }

                    ColumnSpacer(MaterialTheme.dimens.spacing.small)
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
                    content = {
                        Box(
                            Modifier.fillMaxWidth()
                                .minimumInteractiveComponentSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedContent(
                                targetState = playlists.isNotEmpty(),
                            ) { status ->
                                when (status) {
                                    false -> Text(text = "skip for now")
                                    true -> Text(text = "continue")
                                }
                            }
                        }
                    },
                    onClick = {
                        navController.navigate(Screens.SetupScreens.SelectGenre)
                    }
                )

                Spacer(
                    Modifier.navigationBarsPadding()
                        .padding(MaterialTheme.dimens.spacing.medium)
                )
            }
        }
    }
}
