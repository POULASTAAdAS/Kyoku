package com.poulastaa.view.presentation.artist

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.UserIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.view.presentation.artist.components.ViewArtistErrorScreen
import com.poulastaa.view.presentation.artist.components.ViewArtistMediumLoadingScreen
import com.poulastaa.view.presentation.artist.components.ViewArtistTopBar
import com.poulastaa.view.presentation.components.ViewControllerCard
import com.poulastaa.view.presentation.components.ViewSongCard
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewArtistMediumScreen(
    scroll: TopAppBarScrollBehavior,
    state: ViewArtistUiState,
    onAction: (ViewArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    var controllerHeight by remember { mutableIntStateOf(0) }

    val config = LocalConfiguration.current
    val cardHeight = (config.screenHeightDp - (config.screenHeightDp / 2.0).roundToInt()).dp

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        AnimatedContent(targetState = state.loadingType) { loadingState ->
            when (loadingState) {
                LoadingType.Loading -> ViewArtistMediumLoadingScreen(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(colors = ThemModeChanger.getGradiantBackground())
                        )
                        .fillMaxSize()
                )

                is LoadingType.Error -> ViewArtistErrorScreen(
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
                                Box(modifier = Modifier.fillMaxSize()) {
                                    SubcomposeAsyncImage(
                                        model = CacheImageReq.imageReq(
                                            state.artist.cover,
                                            LocalContext.current
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .blur(10.dp),
                                        contentScale = ContentScale.FillBounds,
                                        error = {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = UserIcon,
                                                    contentDescription = null,
                                                    modifier = Modifier.fillMaxSize(.7f),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    )

                                    Card(
                                        modifier = Modifier
                                            .fillMaxSize(.5f)
                                            .aspectRatio(1f)
                                            .align(Alignment.Center),
                                        shape = MaterialTheme.shapes.small,
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 8.dp
                                        ),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer
                                        )
                                    ) {
                                        SubcomposeAsyncImage(
                                            model = CacheImageReq.imageReq(
                                                state.artist.cover,
                                                LocalContext.current
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.FillBounds,
                                            loading = {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    CircularProgressIndicator(
                                                        modifier = Modifier.fillMaxSize(.15f),
                                                        strokeWidth = 3.dp,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            },
                                            error = {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = UserIcon,
                                                        contentDescription = null,
                                                        modifier = Modifier.fillMaxSize(.7f),
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }

                            if (state.loadingType is LoadingType.Content) ViewArtistTopBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                ThemModeChanger.getGradiantBackground().first(),
                                                Color.Transparent,
                                            )
                                        )
                                    )
                                    .padding(end = MaterialTheme.dimens.medium1)
                                    .padding(top = MaterialTheme.dimens.medium2),
                                scrollBehavior = scroll,
                                artist = state.artist,
                                onAction = onAction,
                                navigateBack = navigateBack
                            )
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
                                state = state,
                                onAction = onAction
                            )
                        }
                    }

                    items(state.mostPopularSongs) { song ->
                        ViewSongCard(
                            controllerHeight = controllerHeight,
                            song = song,
                            cardHeight = 90.dp,
                            onSongClick = { type, songId ->
                                onAction(ViewArtistUiAction.OnSongClick(type, songId))
                            },
                            onSongThreeDotClick = { songId ->
                                onAction(ViewArtistUiAction.OnSongOptionsToggle(songId))
                            }
                        )
                        Spacer(Modifier.height(MaterialTheme.dimens.small1))
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
)
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewArtistMediumScreen(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = PREV_DATA,
                onAction = {},
                navigateBack = {}
            )
        }
    }
}