package com.poulastaa.play.presentation.player.small_player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.CancelIcon
import com.poulastaa.core.presentation.designsystem.NextIcon
import com.poulastaa.core.presentation.designsystem.PauseIcon
import com.poulastaa.core.presentation.designsystem.PlayIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.presentation.player.PlayerUiEvent
import com.poulastaa.play.presentation.player.PlayerUiSong
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallExpandedPlayer(
    modifier: Modifier = Modifier,
    header: String,
    song: PlayerUiSong,
    hasNext: Boolean,
    hasPrev: Boolean,
    onEvent: (PlayerUiEvent) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            draggedElevation = 6.dp
        ),
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onEvent(PlayerUiEvent.OnPlayerExtendClick)
        }
    ) {
        Column(
            modifier = Modifier
                .background(Brush.verticalGradient(colors = song.colors))
                .padding(MaterialTheme.dimens.small2)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.6f)
                    .padding(MaterialTheme.dimens.small1),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ImageGrid(
                    header = header,
                    urls = listOf(song.coverImage),
                    shapes = MaterialTheme.shapes.extraSmall,
                    modifier = Modifier.aspectRatio(1f),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                )

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                Column {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.End)
                            .clip(CircleShape)
                            .clickable {
                                onEvent(PlayerUiEvent.ClosePlayer)
                            },
                        imageVector = CancelIcon,
                        contentDescription = null,
                        tint = song.colors[1]
                    )

                    Row {
                        PlayControlButton(
                            icon = NextIcon,
                            color = IconButtonDefaults.iconButtonColors(
                                contentColor = song.colors[1]
                            ),
                            enable = hasPrev,
                            modifier = Modifier
                                .rotate(90f)
                                .aspectRatio(.5f),
                            onClick = {
                                onEvent(PlayerUiEvent.PlayBackController.OnPlayPrevClick)
                            }
                        )

                        Spacer(Modifier.weight(1f))

                        PlayControlButton(
                            icon = if (song.isPlaying) PauseIcon else PlayIcon,
                            color = IconButtonDefaults.iconButtonColors(
                                contentColor = song.colors[1]
                            ),
                            modifier = Modifier
                                .aspectRatio(.6f),
                            onClick = {
                                onEvent(PlayerUiEvent.PlayBackController.OnPlayPause(song.songId))
                            }
                        )

                        Spacer(Modifier.weight(1f))

                        PlayControlButton(
                            icon = NextIcon,
                            color = IconButtonDefaults.iconButtonColors(
                                contentColor = song.colors[1]
                            ),
                            enable = hasNext,
                            modifier = Modifier
                                .aspectRatio(.5f),
                            onClick = {
                                onEvent(PlayerUiEvent.PlayBackController.OnPlayNextClick)
                            }
                        )
                    }
                }
            }

            Slider(
                value = song.progress,
                modifier = Modifier.weight(.1f),
                onValueChange = {
                    onEvent(PlayerUiEvent.PlayBackController.SeekTo(it))
                },
                valueRange = 0f..100f,
                track = {
                    SliderDefaults.Track(
                        sliderState = it,
                        modifier = Modifier
                            .height(5.dp)
                            .fillMaxWidth(),
                        thumbTrackGapSize = 0.dp,
                        drawStopIndicator = null
                    )
                },
                thumb = {
                    SliderDefaults.Thumb(
                        modifier = Modifier.offset(y = 3.dp),
                        interactionSource = remember { MutableInteractionSource() },
                        thumbSize = DpSize(15.dp, 10.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = song.colors[1],
                        )
                    )
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.small1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = song.title,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = song.artist,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        color = MaterialTheme.colorScheme.onBackground.copy(.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.weight(1f))


                Icon(
                    imageVector = ArrowDownIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(180f)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(MaterialTheme.dimens.large1)
        ) {
            SmallExpandedPlayer(
                modifier = Modifier
                    .height(130.dp)
                    .width(200.dp)
                    .clickable { },
                header = "",
                hasNext = false,
                hasPrev = true,
                song = PlayerUiSong(
                    title = "That cool song",
                    artist = "That Cool artist",
                    endTime = "4:00",
                    progress = 13.4f,
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.surfaceContainer
                    )
                )
            ) { }
        }
    }
}