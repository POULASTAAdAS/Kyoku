package com.poulastaa.play.presentation.player.full_player

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.poulastaa.core.domain.RepeatState
import com.poulastaa.core.presentation.designsystem.AddToLibraryIcon
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.FavouriteIcon
import com.poulastaa.core.presentation.designsystem.MoveIcon
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
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    header: String,
    song: PlayerUiSong,
    info: PlayerUiInfo,
    queue: List<PlayerUiSong>,
    onEvent: (PlayerUiEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()
    var columnVisible by remember { mutableStateOf(true) }
    var columnPosition by remember { mutableStateOf(Rect.Zero) }
    val density = LocalDensity.current

    val pagerState = rememberPagerState { 2 }

    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset }.collect {
            val viewportHeight =
                with(density) { 600.dp.toPx() }
            columnVisible = columnPosition.top >= 0 && columnPosition.bottom <= viewportHeight
        }
    }

    Log.d("columnVisible", columnVisible.toString())

    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(colors = song.colors)
                )
                .padding(it)
                .onGloballyPositioned { layoutCoordinates ->
                    columnPosition = Rect(
                        offset = Offset(
                            layoutCoordinates.positionInWindow().x,
                            layoutCoordinates.positionInWindow().y
                        ),
                        size = layoutCoordinates.size.toSize()
                    )
                }
                .verticalScroll(rememberScrollState())
        ) {
            TopBar(
                colors = song.colors,
                type = info.type,
                onEvent = onEvent
            )

            Spacer(Modifier.height(MaterialTheme.dimens.large1))

            ImageGrid(
                header = header,
                urls = listOf(song.coverImage),
                modifier = Modifier
                    .padding(MaterialTheme.dimens.medium3)
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                shapes = MaterialTheme.shapes.small
            )

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            Row(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(.8f)
                ) {
                    Text(
                        text = song.title,
                        fontWeight = FontWeight.SemiBold,
                        color = song.colors[0],
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = song.artist,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = song.colors[0],
                    )
                }

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = {

                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.dimens.small1),
                        imageVector = if (song.isInFavourite) FavouriteIcon
                        else Icons.Rounded.FavoriteBorder,
                        contentDescription = null,
                    )
                }
            }

            Spacer(Modifier.height(MaterialTheme.dimens.large2))

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
                        drawStopIndicator = null
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
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-15).dp)
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = song.currentProgress,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = song.colors[0]
                )
                Text(
                    text = song.endTime,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = song.colors[0]
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.large2))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomIconButton(
                    modifier = Modifier.size(35.dp),
                    icon = if (info.repeatState == RepeatState.SINGLE) RepeatOnIcon else RepeatOffIcon,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (info.repeatState == RepeatState.IDLE)
                            song.colors[0].copy(.5f)
                        else song.colors[0]
                    )
                ) {

                }

                CustomIconButton(
                    modifier = Modifier
                        .size(50.dp)
                        .rotate(90f),
                    icon = NextIcon,
                    enabled = info.hasPrev,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = song.colors[0]
                    )
                ) {

                }

                CustomIconButton(
                    modifier = Modifier.size(90.dp),
                    icon = if (song.isPlaying) PauseIcon else PlayIcon,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = song.colors[0]
                    )
                ) {

                }

                CustomIconButton(
                    modifier = Modifier.size(50.dp),
                    icon = NextIcon,
                    enabled = info.hasNext,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = song.colors[0]
                    )
                ) {

                }

                CustomIconButton(
                    modifier = Modifier.size(35.dp),
                    icon = AddToLibraryIcon,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = song.colors[0]
                    )
                ) {

                }
            }

            Spacer(Modifier.height(MaterialTheme.dimens.large1))
            Spacer(Modifier.height(MaterialTheme.dimens.large1))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Header(
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                scope.launch {
                                    if (pagerState.currentPage != 0)
                                        pagerState.animateScrollToPage(0)
                                }
                            }
                        ),
                    isSelected = pagerState.settledPage == 0,
                    text = stringResource(R.string.playing_queue),
                    color = song.colors[0]
                )

                Header(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                scope.launch {
                                    if (pagerState.currentPage != 1)
                                        pagerState.animateScrollToPage(1)
                                }
                            }
                        ),
                    isSelected = pagerState.settledPage == 1,
                    text = stringResource(R.string.song_info),
                    color = song.colors[0]
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            HorizontalPager(
                state = pagerState
            ) { index ->
                if (index == 0) Queue(
                    id = song.id,
                    header = header,
                    colors = song.colors,
                    queue = queue,
                    onEvent = onEvent
                ) else Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .padding(horizontal = MaterialTheme.dimens.medium1)
                        .padding(bottom = MaterialTheme.dimens.medium1),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 7.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = song.colors[1],
                    )
                ) {

                }
            }
        }
    }
}

@Composable
private fun Queue(
    id: Long,
    header: String,
    colors: List<Color>,
    queue: List<PlayerUiSong>,
    onEvent: (PlayerUiEvent) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(horizontal = MaterialTheme.dimens.medium1)
            .padding(bottom = MaterialTheme.dimens.medium1),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = colors[1],
        )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = MaterialTheme.dimens.medium1),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
        ) {
            items(queue.size) {
                SongCard(
                    modifier = Modifier.clickable {

                    },
                    header = header,
                    colors = if (queue[it].id == id) colors else
                        colors.map { color -> color.copy(.5f) },
                    song = queue[it],
                    onMove = {
                        if (queue[it].id != id) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SongCard(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    header: String,
    song: PlayerUiSong,
    onMove: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .fillMaxWidth()
            .height(70.dp),
    ) {
        Icon(
            imageVector = MoveIcon,
            contentDescription = null,
            tint = colors[0],
            modifier = Modifier
                .fillMaxHeight()
                .combinedClickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {},
                    onLongClick = onMove
                )
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.small1))

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageGrid(
                header = header,
                urls = listOf(song.coverImage),
                elevation = CardDefaults.cardElevation()
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.9f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.SemiBold,
                    color = colors[0],
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = song.artist,
                    color = colors[0],
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.weight(1f))


        }
    }
}

@Composable
private fun CustomIconButton(
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        enabled = enabled
    ) {
        Icon(
            modifier = modifier,
            imageVector = icon,
            contentDescription = null
        )
    }
}

@Composable
private fun Header(
    modifier: Modifier,
    isSelected: Boolean,
    text: String,
    color: Color
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = if (isSelected) color else Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )

        Spacer(modifier = Modifier.height(2.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    color = if (isSelected) color else Color.Transparent
                )
        )
    }
}

@Composable
private fun TopBar(
    colors: List<Color>,
    type: String,
    onEvent: (PlayerUiEvent.OnPlayerShrinkClick) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 56.dp)
            .padding(horizontal = MaterialTheme.dimens.small3),
    ) {
        IconButton(
            onClick = {
                onEvent(PlayerUiEvent.OnPlayerShrinkClick)
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = colors[1]
            )
        ) {
            Icon(
                imageVector = ArrowDownIcon,
                contentDescription = null,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.playing_from),
                fontWeight = FontWeight.SemiBold,
                color = colors[1]
            )

            Text(
                text = type,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colors[1]
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        PlayerScreen(
            header = "",
            song = PlayerUiSong(
                id = 1,
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
                    id = it.toLong(),
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