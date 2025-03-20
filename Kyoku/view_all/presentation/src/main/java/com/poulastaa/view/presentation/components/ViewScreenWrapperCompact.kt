package com.poulastaa.view.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.PlayType
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.ui.UserIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.view.presentation.model.UiViewPrevSong
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewScreenWrapperCompact(
    scroll: TopAppBarScrollBehavior,
    data: List<UiViewPrevSong>,
    name: String,
    cover: String,
    covers: List<String>? = null,
    totalSongs: Int,
    loadingType: LoadingType,
    isTypeArtist: Boolean,
    isNotAlbum: Boolean,
    songCardHeight: Dp = 75.dp,
    onExplore: () -> Unit = {},
    play: (type: PlayType) -> Unit,
    onSongClick: (clickType: ItemClickType, songId: SongId) -> Unit,
    onSongThreeDotClick: (songId: SongId) -> Unit,
    topBar: @Composable () -> Unit,
) {
    var controllerHeight by remember { mutableIntStateOf(0) }

    val config = LocalConfiguration.current
    val cardHeight = (config.screenHeightDp - (config.screenHeightDp / 2.0).roundToInt()).dp

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        AnimatedContent(targetState = loadingType) { loadingState ->
            when (loadingState) {
                LoadingType.Loading -> ViewCompactLoadingScreen(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(colors = ThemModeChanger.getGradiantBackground())
                        )
                        .fillMaxSize(),
                    isArtist = isTypeArtist
                )

                is LoadingType.Error -> ViewErrorScreen(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(colors = ThemModeChanger.getGradiantBackground())
                        )
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(MaterialTheme.dimens.medium1),
                    error = loadingState
                )

                LoadingType.Content -> LazyColumn(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(paddingValues)
                        .nestedScroll(scroll.nestedScrollConnection),
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(cardHeight),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                shape = RectangleShape,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                ViewCoverCard(isNotAlbum, isTypeArtist, covers, cover)
                            }

                            topBar()
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onSizeChanged {
                                    controllerHeight = (it.height / 2)
                                }
                                .offset {
                                    IntOffset(y = -controllerHeight, x = 0)
                                }
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            ThemModeChanger.getGradiantBackground().first(),
                                            ThemModeChanger.getGradiantBackground().first(),
                                            Color.Transparent,
                                        )
                                    )
                                )
                                .padding(horizontal = MaterialTheme.dimens.medium1)
                        ) {
                            ViewControllerCard(
                                name = name,
                                totalSongs = totalSongs,
                                isArtist = isTypeArtist,
                                onExplore = onExplore,
                                play = play,
                            )
                        }
                    }

                    items(data) { song ->
                        ViewSongCard(
                            controllerHeight = controllerHeight,
                            cardHeight = songCardHeight,
                            song = song,
                            onSongClick = onSongClick,
                            onSongThreeDotClick = onSongThreeDotClick
                        )

                        Spacer(Modifier.height(MaterialTheme.dimens.small2))
                    }
                }
            }
        }
    }
}