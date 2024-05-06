package com.poulastaa.kyoku.presentation.screen.home_root.player

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.Player
import com.poulastaa.kyoku.data.model.home_nav_drawer.PlayingSongData
import com.poulastaa.kyoku.data.model.home_nav_drawer.QueueSong
import com.poulastaa.kyoku.data.model.screens.player.PlayerSong
import com.poulastaa.kyoku.data.model.screens.view_aritst.ViewArtistUiArtist
import com.poulastaa.kyoku.presentation.common.ArtistView
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomImageView
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Player(
    isSmallPhone: Boolean,
    paddingValue: PaddingValues,
    player: Player,
    isDarkThem: Boolean,
    isCookie: Boolean,
    header: String,
    scope: CoroutineScope,
    brushColor: List<Color> = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.background,
        MaterialTheme.colorScheme.background,
    ),
    loadAdditionalData: () -> Unit,
    onDurationChange: (Float) -> Unit,
    playControl: (HomeRootUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = brushColor.take(2)
                )
            )
            .padding(
                top = paddingValue.calculateTopPadding(),
                bottom = paddingValue.calculateBottomPadding(),
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(end = 56.dp - MaterialTheme.dimens.medium1),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Row(
                    modifier = Modifier.wrapContentSize()
                ) {
                    IconButton(
                        onClick = navigateBack,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = brushColor[1],
                            contentColor = brushColor[0]
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Playing from",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = brushColor[1]
                    )
                    Text(
                        text = player.info.typeName,
                        fontWeight = FontWeight.Light,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = brushColor[1]
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
        }

        item {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.small
            ) {
                CustomImageView(
                    modifier = Modifier.aspectRatio(1f),
                    isDarkThem = isDarkThem,
                    isCookie = isCookie,
                    headerValue = header,
                    url = player.playingSongData.playingSong.url,
                    contentScale = ContentScale.Crop
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(.8f)
                ) {
                    Text(
                        text = player.playingSongData.playingSong.title,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        fontWeight = FontWeight.Medium,
                        color = brushColor[0],
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = player.playingSongData.playingSong.artist.toString().trimStart('[')
                            .trimEnd(']'),
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = brushColor[0],
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.large2)
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Slider(
                    value = player.progress,
                    onValueChange = onDurationChange,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = brushColor[0],
                        activeTrackColor = brushColor[0],
                        inactiveTrackColor = brushColor[1]
                    ),
                    valueRange = 0f..100f
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-15).dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = player.playingSongData.playingSong.currentInMin,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = brushColor[0]
                    )
                    Text(
                        text = player.playingSongData.playingSong.totalTime,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = brushColor[0]
                    )
                }
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.medium1)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomIconButton(
                    modifier = Modifier.size(35.dp),
                    icon = R.drawable.ic_repeat_off,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = brushColor[0]
                    )
                ) {
                    // todo add shuffle
//playControl.invoke(HomeRootUiEvent)
                }

                CustomIconButton(
                    modifier = Modifier
                        .size(50.dp)
                        .rotate(90f),
                    icon = R.drawable.ic_next,
                    enabled = player.hasPrev,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = brushColor[0]
                    )
                ) {
                    playControl.invoke(HomeRootUiEvent.PlayerUiEvent.Backward)
                }

                CustomIconButton(
                    modifier = Modifier.size(if (isSmallPhone) 70.dp else 90.dp),
                    icon = if (player.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = brushColor[0]
                    )
                ) {
                    playControl.invoke(HomeRootUiEvent.PlayerUiEvent.PlayPause)
                }

                CustomIconButton(
                    modifier = Modifier.size(50.dp),
                    icon = R.drawable.ic_next,
                    enabled = player.hasNext,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = brushColor[0]
                    )
                ) {
                    playControl.invoke(HomeRootUiEvent.PlayerUiEvent.Forward)
                }

                CustomIconButton(
                    modifier = Modifier.size(35.dp),
                    icon = R.drawable.ic_add_to_library,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = brushColor[0]
                    )
                ) {
// todo add library option
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
        }

        item {
            val pagerState = rememberPagerState { 2 }
            val interactionSource = remember {
                MutableInteractionSource()
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
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
                                        if (pagerState.currentPage != 0) pagerState.animateScrollToPage(
                                            0
                                        )
                                    }
                                }
                            ),
                        isSelected = pagerState.settledPage == 0,
                        text = "Playing Queue",
                        color = brushColor[0]
                    )

                    Header(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    scope.launch {
                                        if (pagerState.currentPage != 1) pagerState.animateScrollToPage(
                                            1
                                        )
                                    }
                                }
                            ),
                        isSelected = pagerState.settledPage == 1,
                        text = "Song Info",
                        color = brushColor[0]
                    )
                }

                HorizontalPager(
                    state = pagerState
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                        if (it == 0) AllQueueSong(
                            allSong = player.allSong,
                            isDarkThem = isDarkThem,
                            isCookie = isCookie,
                            header = header,
                            brushColor = brushColor,
                            onSongClick = {

                            },
                            onAddClick = {

                            }
                        )
                        else {
                            LaunchedEffect(key1 = player.playingSongData.isAdditionalDataLoaded) {
                                if (!player.playingSongData.isAdditionalDataLoaded)
                                    loadAdditionalData.invoke()
                            }

                            if (!player.playingSongData.isAdditionalDataLoaded)
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(MaterialTheme.dimens.small3),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                }
                            else
                                SongInfo(
                                    songData = player.playingSongData,
                                    isDarkThem = isDarkThem,
                                    isCookie = isCookie,
                                    header = header,
                                    onArtistClick = {

                                    },
                                    onAlbumClick = {

                                    }
                                )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
                    }
                }
            }
        }
    }

    BackHandler {
        navigateBack.invoke()
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
private fun AllQueueSong(
    allSong: List<QueueSong>,
    isDarkThem: Boolean,
    isCookie: Boolean,
    header: String,
    brushColor: List<Color>,
    onSongClick: (songId: Long) -> Unit,
    onAddClick: (songId: Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        allSong.forEach {
            PlayingQueueSong(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .clickable(
                        onClick = {
                            onSongClick.invoke(it.playerSong.id)
                        }
                    ),
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                headerValue = header,
                brushColor = brushColor,
                song = it,
                isPlaying = it.isPlaying,
                onAddClick = onAddClick
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }
    }
}

@Composable
private fun PlayingQueueSong(
    modifier: Modifier,
    brushColor: List<Color>,
    song: QueueSong,
    isPlaying: Boolean,
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    onAddClick: (songId: Long) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        CustomImageView(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.2f)
                .clip(MaterialTheme.shapes.extraSmall),
            isDarkThem = isDarkThem,
            isCookie = isCookie,
            headerValue = headerValue,
            url = song.playerSong.url
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = song.playerSong.title,
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = if (isPlaying) brushColor[2] else Color.Unspecified
            )

            Text(
                text = song.playerSong.artist.toString().trimStart('[').trimEnd(']'),
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                color = if (isPlaying) brushColor[2] else Color.Unspecified
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                modifier = Modifier.size(40.dp),
                onClick = { onAddClick.invoke(song.playerSong.id) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = brushColor[2]
                )
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun SongInfo(
    songData: PlayingSongData,
    isDarkThem: Boolean,
    isCookie: Boolean,
    header: String,
    interactionSource: MutableInteractionSource = remember {
        MutableInteractionSource()
    },
    onArtistClick: (artistId: Long) -> Unit,
    onAlbumClick: (albumId: Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
    ) {
        if (songData.playingSongArtist.isNotEmpty()) {
            Text(
                text = "Artist",
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = MaterialTheme.colorScheme.onBackground
            )

            songData.playingSongArtist.forEach {
                ArtistView(
                    data = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onArtistClick.invoke(it.artistId)
                        },
                    imageSize = 70.dp,
                    isDarkThem = isDarkThem,
                    isCookie = isCookie,
                    header = header,
                    nameFontSize = MaterialTheme.typography.titleMedium.fontSize,
                    listenedFontSize = MaterialTheme.typography.titleSmall.fontSize
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (songData.playingSongAlbum.albumId != -1L) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Album",
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    AlbumView(
                        modifier = Modifier
                            .wrapContentSize()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                onAlbumClick.invoke(songData.playingSongAlbum.albumId)
                            },
                        cover = songData.playingSong.url,
                        isDarkThem = isDarkThem,
                        isCookie = isCookie,
                        header = header,
                        name = songData.playingSongAlbum.name
                    )
                }
            }

            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Release Year",
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = songData.playingSong.year.ifEmpty { songData.releaseDate },
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = 2.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

        Text(
            text = "Some Info may not be totally accurate. Sorry for the Inconvenience.",
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Monospace,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AlbumView(
    modifier: Modifier = Modifier,
    cover: String,
    isDarkThem: Boolean,
    isCookie: Boolean,
    header: String,
    name: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomImageView(
            modifier = Modifier
                .size(70.dp)
                .clip(MaterialTheme.shapes.extraSmall),
            isDarkThem = isDarkThem,
            isCookie = isCookie,
            headerValue = header,
            url = cover
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))

        Text(
            modifier = Modifier.width(70.dp),
            text = name,
            fontWeight = FontWeight.Light,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 2.sp
        )
    }
}


@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    @DrawableRes icon: Int,
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
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    TestThem {
        com.poulastaa.kyoku.presentation.screen.home_root.player.Player(
            isSmallPhone = false,
            paddingValue = PaddingValues(),
            player = Player(
                isPlaying = true,
                allSong = listOf(
                    QueueSong(
                        isPlaying = true,
                        playerSong = PlayerSong(
                            title = "Title",
                            artist = listOf(
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2"
                            ),
                            totalTime = "4.40"
                        )
                    ),
                    QueueSong(
                        isPlaying = false,
                        playerSong = PlayerSong(
                            title = "Title",
                            artist = listOf(
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2"
                            ),
                            totalTime = "4.40"
                        )
                    ), QueueSong(
                        isPlaying = false,
                        playerSong = PlayerSong(
                            title = "Title",
                            artist = listOf(
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2"
                            ),
                            totalTime = "4.40"
                        )
                    ), QueueSong(
                        isPlaying = false,
                        playerSong = PlayerSong(
                            title = "Title",
                            artist = listOf(
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2"
                            ),
                            totalTime = "4.40"
                        )
                    ), QueueSong(
                        isPlaying = false,
                        playerSong = PlayerSong(
                            title = "Title",
                            artist = listOf(
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2",
                                "artist1",
                                "artist2"
                            ),
                            totalTime = "4.40"
                        )
                    )
                ),
                playingSongData = PlayingSongData(
                    playingSong = PlayerSong(
                        title = "Title",
                        artist = listOf(
                            "artist1",
                            "artist2",
                            "artist1",
                            "artist2",
                            "artist1",
                            "artist2",
                            "artist1",
                            "artist2"
                        ),
                        totalTime = "4.40"
                    ),
                    playingSongArtist = listOf(
                        ViewArtistUiArtist(
                            name = "name1",
                            listened = 1200
                        ),
                        ViewArtistUiArtist(
                            name = "name2",
                            listened = 100
                        )
                    )
                )
            ),
            scope = rememberCoroutineScope(),
            isDarkThem = isSystemInDarkTheme(),
            isCookie = false,
            header = "",
            brushColor = listOf(
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.background,
                MaterialTheme.colorScheme.onBackground,
            ),
            playControl = {},
            loadAdditionalData = {},
            onDurationChange = {},

            ) {

        }

//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.tertiaryContainer)
//                .padding(MaterialTheme.dimens.medium1)
//        ) {
//            SongInfo(
//                songData = PlayingSongData(
//                    playingSong = PlayerSong(
//                        year = "2023"
//                    ),
//                    isAdditionalDataLoaded = true,
//                    playingSongArtist = listOf(
//                        ViewArtistUiArtist(
//                            name = "name1",
//                            listened = 1200
//                        ),
//                        ViewArtistUiArtist(
//                            name = "name2",
//                            listened = 100
//                        )
//                    ),
//                    playingSongAlbum = PlayingSongAlbum(
//                        albumId = 1,
//                        name = "albumName",
//                    )
//                ),
//                isDarkThem = false,
//                isCookie = false,
//                header = "",
//                headerColor = MaterialTheme.colorScheme.tertiary,
//                onArtistClick = {},
//            )
//        }
    }
}