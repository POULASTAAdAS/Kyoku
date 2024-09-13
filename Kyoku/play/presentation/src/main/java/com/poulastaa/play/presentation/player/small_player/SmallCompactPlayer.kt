package com.poulastaa.play.presentation.player.small_player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.NextIcon
import com.poulastaa.core.presentation.designsystem.PauseIcon
import com.poulastaa.core.presentation.designsystem.PlayIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.presentation.player.PlayerUiEvent
import com.poulastaa.play.presentation.player.PlayerUiSong
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallCompactPlayer(
    modifier: Modifier = Modifier,
    height: Dp = 95.dp,
    header: String,
    song: PlayerUiSong,
    hasNext: Boolean,
    hasPrev: Boolean,
    onEvent: (PlayerUiEvent.PlayBackController) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(
                brush = Brush.linearGradient(colors = song.colors)
            )
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight(.8f)
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.dimens.small1),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageGrid(
                header = header,
                urls = listOf(song.coverImage),
                shapes = MaterialTheme.shapes.extraSmall,
            )

            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.SemiBold,
                    color = song.colors[1],
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = song.artist,
                    color = song.colors[1],
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            PlayControlButton(
                icon = NextIcon,
                color = IconButtonDefaults.iconButtonColors(
                    contentColor = song.colors[0]
                ),
                enable = hasPrev,
                modifier = Modifier
                    .rotate(90f)
                    .size(35.dp),
                onClick = {
                    onEvent(PlayerUiEvent.PlayBackController.OnPlayPrevClick)
                }
            )

            Spacer(Modifier.width(MaterialTheme.dimens.small2))

            PlayControlButton(
                icon = if (song.isPlaying) PauseIcon else PlayIcon,
                color = IconButtonDefaults.iconButtonColors(
                    contentColor = song.colors[0]
                ),
                modifier = Modifier
                    .size(45.dp),
                onClick = {
                    onEvent(PlayerUiEvent.PlayBackController.OnPlayPause(song.id))
                }
            )

            Spacer(Modifier.width(MaterialTheme.dimens.small2))

            PlayControlButton(
                icon = NextIcon,
                color = IconButtonDefaults.iconButtonColors(
                    contentColor = song.colors[0]
                ),
                enable = hasNext,
                modifier = Modifier
                    .size(35.dp),
                onClick = {
                    onEvent(PlayerUiEvent.PlayBackController.OnPlayNextClick)
                }
            )
        }

        Slider(
            value = song.progress,
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
    }
}

@Composable
fun PlayControlButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    color: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    enable: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        colors = color,
        enabled = enable
    ) {
        Icon(
            modifier = modifier,
            imageVector = icon,
            contentDescription = null
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column {
            SmallCompactPlayer(
                modifier = Modifier
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