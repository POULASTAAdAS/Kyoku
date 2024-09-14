package com.poulastaa.play.presentation.player.full_player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
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
import com.poulastaa.play.presentation.player.small_player.PlayControlButton
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalPlayerScreen(
    modifier: Modifier = Modifier,
    header: String,
    song: PlayerUiSong,
    info: PlayerUiInfo,
    queue: List<PlayerUiSong>,
    onEvent: (PlayerUiEvent) -> Unit,
) {
    val pagerState = rememberPagerState { 2 }
    var controllerPos by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp

    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(colors = song.colors)
                    )
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                PlayerTopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 56.dp)
                        .padding(horizontal = MaterialTheme.dimens.small3),
                    colors = song.colors,
                    type = info.type,
                    onEvent = onEvent
                )

                Spacer(
                    Modifier.height(
                        if (config.screenWidthDp > 980) MaterialTheme.dimens.small1
                        else MaterialTheme.dimens.large1
                    )
                )

                ImageGrid(
                    header = header,
                    urls = listOf(song.coverImage),
                    modifier = Modifier
                        .padding(
                            if (config.screenWidthDp > 980) MaterialTheme.dimens.large2
                            else MaterialTheme.dimens.medium3
                        )
                        .padding(
                            horizontal = if (config.screenWidthDp > 980) MaterialTheme.dimens.large2
                            else MaterialTheme.dimens.medium1
                        ),
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
                    },
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small3)
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

                Spacer(
                    Modifier.height(
                        if (config.screenWidthDp > 980) MaterialTheme.dimens.large1
                        else MaterialTheme.dimens.large2
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.medium1)
                        .onGloballyPositioned { coordinates ->
                            controllerPos = coordinates.positionInWindow().y
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PlayerCustomIconButton(
                        modifier = Modifier.size(35.dp),
                        icon = if (info.repeatState == RepeatState.SINGLE) RepeatOnIcon else RepeatOffIcon,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = if (info.repeatState == RepeatState.IDLE)
                                song.colors[0].copy(.5f)
                            else song.colors[0]
                        )
                    ) {

                    }

                    PlayerCustomIconButton(
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

                    PlayerCustomIconButton(
                        modifier = Modifier.size(90.dp),
                        icon = if (song.isPlaying) PauseIcon else PlayIcon,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = song.colors[0]
                        )
                    ) {

                    }

                    PlayerCustomIconButton(
                        modifier = Modifier.size(50.dp),
                        icon = NextIcon,
                        enabled = info.hasNext,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = song.colors[0]
                        )
                    ) {

                    }

                    PlayerCustomIconButton(
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
                        .padding(horizontal = MaterialTheme.dimens.medium1)
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
                        )
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(song.colors.map { color ->
                                        color.copy(
                                            .5f
                                        )
                                    })
                                ),
                        ) {

                        }
                    }
                }
            }


            FloatingController(
                modifier = Modifier.padding(it),
                screenHeight = screenHeight,
                controllerPos = controllerPos,
                header = header,
                song = song,
                hasNext = info.hasNext,
                hasPrev = info.hasPrev,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun Queue(
    id: Long,
    header: String,
    colors: List<Color>,
    queue: List<PlayerUiSong>,
    onEvent: (PlayerUiEvent) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val config = LocalConfiguration.current
    var cardPos by remember { mutableFloatStateOf(0f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(config.screenHeightDp.dp)
            .padding(horizontal = MaterialTheme.dimens.medium1)
            .padding(bottom = MaterialTheme.dimens.medium1)
            .onGloballyPositioned {
                cardPos = it.positionInWindow().y
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = colors[1],
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(colors.map { it.copy(.3f) })
                ),
            contentPadding = PaddingValues(vertical = MaterialTheme.dimens.medium1),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
            userScrollEnabled = cardPos < 100
        ) {
            items(queue.size) {
                PlayerSongCard(
                    modifier = Modifier.clickable {
                        onEvent(PlayerUiEvent.PlayBackController.OnSongClick(queue[it].id))
                    },
                    header = header,
                    colors = if (queue[it].id == id) colors else
                        colors.map { color -> color.copy(.7f) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FloatingController(
    modifier: Modifier = Modifier,
    screenHeight: Int,
    controllerPos: Float,
    header: String,
    song: PlayerUiSong,
    hasNext: Boolean,
    hasPrev: Boolean,
    onEvent: (PlayerUiEvent.PlayBackController) -> Unit,
) {
    val config = LocalConfiguration.current
    val visibilityThreshold = if (config.screenWidthDp > 980) 300 else 250

    AnimatedVisibility(
        modifier = Modifier.fillMaxWidth(),
        visible = controllerPos < visibilityThreshold,
        enter = fadeIn() + expandIn(expandFrom = Alignment.Center) +
                slideInVertically(tween(400)) { -it },
        exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center) +
                slideOutVertically(tween(400)) { it }
    ) {
        val temp = remember { controllerPos < visibilityThreshold }

        if (temp) Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier)
                .height((screenHeight * (if (config.screenWidthDp > 980) .9f else .7f) / 4).dp)
                .padding(horizontal = MaterialTheme.dimens.medium1),
            elevation = CardDefaults.cardElevation(defaultElevation = 7.dp),
            shape = MaterialTheme.shapes.small
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(song.colors[1])
                    .padding(MaterialTheme.dimens.small3),
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
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                    )

                    Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = song.title,
                            fontWeight = FontWeight.SemiBold,
                            color = song.colors[0]
                        )

                        Text(
                            text = song.artist,
                            color = song.colors[0].copy(.5f),
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )


                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            PlayControlButton(
                                icon = NextIcon,
                                color = IconButtonDefaults.iconButtonColors(
                                    contentColor = song.colors[0]
                                ),
                                enable = hasPrev,
                                modifier = Modifier
                                    .aspectRatio(.8f)
                                    .rotate(90f),
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
                                modifier = Modifier.aspectRatio(1f),
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
                                modifier = Modifier.aspectRatio(.8f),
                                onClick = {
                                    onEvent(PlayerUiEvent.PlayBackController.OnPlayNextClick)
                                }
                            )
                        }

                        Spacer(Modifier.weight(.5f))
                    }
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
                                thumbColor = song.colors[0],
                            )
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerSongCard(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    header: String,
    song: PlayerUiSong,
    onMove: () -> Unit,
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
                    interactionSource = null,
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
fun PlayerCustomIconButton(
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit,
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
    color: Color,
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
fun PlayerTopBar(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    type: String,
    onEvent: (PlayerUiEvent.OnPlayerShrinkClick) -> Unit,
) {
    Row(
        modifier = modifier,
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
        VerticalPlayerScreen(
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