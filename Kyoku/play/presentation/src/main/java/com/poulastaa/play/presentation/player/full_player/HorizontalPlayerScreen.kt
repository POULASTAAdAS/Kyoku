package com.poulastaa.play.presentation.player.full_player

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.poulastaa.core.domain.RepeatState
import com.poulastaa.core.presentation.designsystem.AddToLibraryIcon
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.FilterPlaylistIcon
import com.poulastaa.core.presentation.designsystem.InfoIcon
import com.poulastaa.core.presentation.designsystem.NextIcon
import com.poulastaa.core.presentation.designsystem.PauseIcon
import com.poulastaa.core.presentation.designsystem.PlayIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.RepeatOffIcon
import com.poulastaa.core.presentation.designsystem.RepeatOnIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.presentation.player.PlayerUiEvent
import com.poulastaa.play.presentation.player.PlayerUiInfo
import com.poulastaa.play.presentation.player.PlayerUiSong
import com.poulastaa.play.presentation.player.components.PlayerCustomIconButton
import com.poulastaa.play.presentation.player.components.PlayerSongCard
import com.poulastaa.play.presentation.player.components.PlayerSongInfo
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorizontalPlayerScreen(
    header: String,
    song: PlayerUiSong,
    info: PlayerUiInfo,
    queue: List<PlayerUiSong>,
    onEvent: (PlayerUiEvent) -> Unit,
) {
    val pagerState = rememberPagerState { 2 }
    var controllerPos by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val list = animateFloatAsState(
        if (pagerState.currentPage == 0) 1f else .23f,
        tween(400),
        label = ""
    )
    val details = animateFloatAsState(
        if (pagerState.currentPage == 1) 1f else .23f,
        tween(400),
        label = ""
    )

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.horizontalGradient(colors = song.colors))
                .padding(it)
                .padding(horizontal = MaterialTheme.dimens.medium3)
                .padding(bottom = MaterialTheme.dimens.medium1),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(.9f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.4f)
                        .fillMaxHeight()
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .weight(.5f),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(song.colors[1])
                                .clickable {
                                    onEvent(PlayerUiEvent.OnPlayerShrinkClick)
                                },
                            imageVector = ArrowDownIcon,
                            contentDescription = null,
                            tint = song.colors[0]
                        )

                        Spacer(Modifier.weight(1f))

                        ImageGrid(
                            header = header,
                            urls = listOf(song.coverImage),
                            shapes = MaterialTheme.shapes.small
                        )

                        Spacer(Modifier.weight(1f))

                        Spacer(Modifier.width(40.dp))
                    }

                    PlayerSongInfo(
                        modifier = Modifier.weight(.3f),
                        song = song.copy(
                            colors = song.colors.reversed()
                        ),
                        onEvent = onEvent
                    )

                    Row(
                        modifier = Modifier
                            .weight(.3f)
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                controllerPos = coordinates.positionInWindow().y
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PlayerCustomIconButton(
                            modifier = Modifier
                                .aspectRatio(.4f),
                            icon = if (info.repeatState == RepeatState.SINGLE) RepeatOnIcon else RepeatOffIcon,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = if (info.repeatState == RepeatState.IDLE)
                                    song.colors[1].copy(.5f)
                                else song.colors[1]
                            )
                        ) {

                        }

                        PlayerCustomIconButton(
                            modifier = Modifier
                                .rotate(90f)
                                .aspectRatio(.6f),
                            icon = NextIcon,
                            enabled = info.hasPrev,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = song.colors[1]
                            )
                        ) {

                        }

                        PlayerCustomIconButton(
                            modifier = Modifier
                                .aspectRatio(.7f),
                            icon = if (song.isPlaying) PauseIcon else PlayIcon,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = song.colors[1]
                            )
                        ) {

                        }

                        PlayerCustomIconButton(
                            modifier = Modifier
                                .aspectRatio(.6f),
                            icon = NextIcon,
                            enabled = info.hasNext,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = song.colors[1]
                            )
                        ) {

                        }

                        PlayerCustomIconButton(
                            modifier = Modifier
                                .aspectRatio(.4f),
                            icon = AddToLibraryIcon,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = song.colors[1]
                            )
                        ) {

                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = MaterialTheme.dimens.medium1,
                            bottom = MaterialTheme.dimens.small2
                        )
                ) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        song.colors
                                            .reversed()
                                            .map { color -> color.copy(.3f) }
                                    )
                                ),
                        ) {
                            VerticalPager(state = pagerState) { index ->
                                if (index == 0) Queue(
                                    haptic = haptic,
                                    header = header,
                                    playingSong = song,
                                    info = info,
                                    queue = queue,
                                    onEvent = onEvent,
                                ) else SongInfoCard(
                                    song = song,
                                    onEvent = onEvent
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = MaterialTheme.dimens.small3)
                                    .padding(vertical = MaterialTheme.dimens.large1),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(list.value),
                                    shape = MaterialTheme.shapes.large,
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 5.dp,
                                        pressedElevation = 0.dp
                                    ),
                                    onClick = {
                                        scope.launch {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            if (pagerState.currentPage != 0)
                                                pagerState.animateScrollToPage(0)
                                        }
                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(song.colors[1])
                                            .padding(MaterialTheme.dimens.small2),
                                        imageVector = FilterPlaylistIcon,
                                        contentDescription = null,
                                        tint = song.colors[0]
                                    )
                                }

                                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(details.value),
                                    shape = MaterialTheme.shapes.large,
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 5.dp,
                                        pressedElevation = 0.dp
                                    ),
                                    onClick = {
                                        scope.launch {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            if (pagerState.currentPage != 1)
                                                pagerState.animateScrollToPage(1)
                                        }
                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(song.colors[1])
                                            .padding(MaterialTheme.dimens.small2),
                                        imageVector = InfoIcon,
                                        contentDescription = null,
                                        tint = song.colors[0]
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = song.currentProgress,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = song.colors[1]
                )
                Text(
                    text = song.endTime,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = song.colors[0]
                )
            }

            Slider(
                value = song.progress,
                onValueChange = { pos ->
                    onEvent(PlayerUiEvent.PlayBackController.SeekTo(pos))
                },
                valueRange = 0f..100f,
                track = { sliderState ->
                    SliderDefaults.Track(
                        sliderState = sliderState,
                        modifier = Modifier
                            .height(5.dp)
                            .fillMaxWidth(),
                        thumbTrackGapSize = 0.dp,
                        drawStopIndicator = null,
                        colors = SliderDefaults.colors(
                            activeTrackColor = if (song.progress > 60f) song.colors[0] else song.colors[1],
                        )
                    )
                },
                thumb = {
                    SliderDefaults.Thumb(
                        interactionSource = remember { MutableInteractionSource() },
                        thumbSize = DpSize(16.dp, 16.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = song.colors[1],
                        )
                    )
                },
                modifier = Modifier.weight(.1f)
            )
        }
    }
}

@Composable
private fun SongInfoCard(
    song: PlayerUiSong,
    onEvent: (PlayerUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(.85f)
            .padding(MaterialTheme.dimens.medium1),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.song),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
            )

            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

            Text(
                text = song.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(Modifier.height(MaterialTheme.dimens.small1))

        HorizontalDivider(
            thickness = 1.dp,
            color = song.colors[0]
        )
    }
}

@Composable
private fun Queue(
    haptic: HapticFeedback,
    header: String,
    playingSong: PlayerUiSong,
    info: PlayerUiInfo,
    queue: List<PlayerUiSong>,
    onEvent: (PlayerUiEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(.85f),
        contentPadding = PaddingValues(vertical = MaterialTheme.dimens.medium1),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.dimens.medium1,
                        bottom = MaterialTheme.dimens.small3
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    text = stringResource(R.string.playing_from),
                    fontWeight = FontWeight.Medium,
                    color = playingSong.colors[0],
                    textDecoration = TextDecoration.Underline
                )

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                Text(
                    text = info.type,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    color = playingSong.colors[0]
                )
            }
        }

        items(queue.size) { index ->
            PlayerSongCard(
                modifier = Modifier.clickable {
                    onEvent(
                        PlayerUiEvent.PlayBackController.OnSongClick(
                            queue[index].songId
                        )
                    )
                },
                header = header,
                colors = if (queue[index].songId == playingSong.songId) playingSong.colors else
                    playingSong.colors.map { color -> color.copy(.4f) },
                song = queue[index],
                onMove = {
                    if (queue[index].songId != playingSong.songId) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                }
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 840,
    heightDp = 400
)
@Preview(
    widthDp = 840,
    heightDp = 400
)
@Composable
private fun Preview() {
    AppThem {
        HorizontalPlayerScreen(
            header = "",
            song = PlayerUiSong(
                songId = 1,
                title = "That cool song",
                artist = "That Cool artist",
                endTime = "4:00",
                progress = 13.4f,
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.surfaceContainer
                )
            ),
            info = PlayerUiInfo(
                type = "album",
                hasNext = true,
                hasPrev = true,
                repeatState = RepeatState.IDLE
            ),
            queue = (1..10).map {
                PlayerUiSong(
                    songId = it.toLong(),
                    title = "That cool song",
                    artist = "That Cool artist",
                    endTime = "4:00",
                    progress = 13.4f,
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.surfaceContainer
                    )
                )
            }
        ) { }
    }
}