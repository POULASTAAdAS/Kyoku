package com.poulastaa.setup.ui.import_spotify_playlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
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
import com.poulastaa.common.ui.components.ElevatedGradientButton
import com.poulastaa.common.ui.components.RowSpacer
import com.poulastaa.common.ui.design_system.IconAddLink
import com.poulastaa.common.ui.design_system.IconArrowBack
import com.poulastaa.common.ui.design_system.IconClose
import com.poulastaa.common.ui.design_system.StringBackButton
import com.poulastaa.common.ui.design_system.dimens
import kotlin.random.Random

@Stable
@Immutable
data class UiDuration(
    val time: String,
    val unit: UiTimeUnit,
) {
    enum class UiTimeUnit(val value: String) {
        HOURS("hours"),
        MINUTES("minutes"),
    }
}

@Stable
@Immutable
data class UiPlaylist(
    val id: Long,
    val title: String,
    val totalDuration: UiDuration,
    val poster: List<String>,
    val tracks: List<UiImportPlaylistTrack>,
    val isExpanded: Boolean = Random.nextBoolean(),
)

@Stable
@Immutable
data class UiImportPlaylistTrack(
    val id: Long,
    val title: String,
    val duration: UiDuration,
    val artist: List<String>,
    val poster: String? = null,
)

val dummyData = (1..3).map {
    UiPlaylist(
        id = it.toLong(),
        title = "Playlist $it",
        totalDuration = UiDuration(
            time = Random.nextInt(5, 15).toString(),
            unit = UiDuration.UiTimeUnit.HOURS
        ),
        poster = emptyList(),
        tracks = (1..Random.nextInt(5, 10)).map {
            UiImportPlaylistTrack(
                id = it.toLong(),
                title = "Track $it",
                duration = UiDuration(
                    time = "5",
                    unit = UiDuration.UiTimeUnit.MINUTES
                ),
                artist = listOf("That Cool Artist"),
                poster = null
            )
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ImportSpotifyPlaylist() {
    val focusManager = LocalFocusManager.current
    var isLoading by mutableStateOf(false)
    var link by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.dimens.layout.contentPadding)
            .systemBarsPadding(),
    ) {
        Text(
            style = MaterialTheme.typography.headlineMedium,
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
                onValueChange = { link = it },
                modifier = Modifier.weight(1f),
                label = "playlist link",
                leadingIcon = IconAddLink,
                trailingContent = {
                    AnimatedVisibility(visible = link.isNotEmpty()) {
                        Icon(
                            imageVector = IconClose,
                            contentDescription = "Clear",
                            modifier = Modifier.clip(CircleShape).clickable(
                                onClick = {
                                    link = ""
                                }
                            )
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        isLoading = true
                    }
                ),
            )

            RowSpacer(MaterialTheme.dimens.spacing.medium)

            Box(Modifier.fillMaxWidth(0.3f)) {
                ElevatedGradientButton(
                    width = 1f,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        if (isLoading.not()) {
                            isLoading = true
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
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                thickness = 0.5.dp
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

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                thickness = 0.5.dp
            )
        }

        ColumnSpacer(MaterialTheme.dimens.spacing.large)

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = dummyData,
                key = { it.id }
            ) { playlist ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(MaterialTheme.dimens.spacing.small)
                            .height(60.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxHeight()
                                .aspectRatio(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp
                            ),
                            shape = MaterialTheme.shapes.small
                        ) {

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

                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                        ) {
                            Box(
                                Modifier.minimumInteractiveComponentSize()
                                    .padding(end = 4.dp)
                                    .rotate(if (playlist.isExpanded) 180f else 90f),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = IconArrowBack,
                                    contentDescription = StringBackButton,
                                )
                            }
                        }
                    }
                }

                ColumnSpacer(MaterialTheme.dimens.spacing.small)
            }
        }
    }
}