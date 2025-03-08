package com.poulastaa.view.presentation.artist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.PlayType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.PlayIcon
import com.poulastaa.core.presentation.designsystem.ui.ShuffleIcon
import com.poulastaa.core.presentation.designsystem.ui.SongIcon
import com.poulastaa.core.presentation.designsystem.ui.ThreeDotIcon
import com.poulastaa.core.presentation.designsystem.ui.UserIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.view.presentation.artist.components.ViewArtistCompactLoadingScreen
import com.poulastaa.view.presentation.artist.components.ViewArtistErrorScreen
import com.poulastaa.view.presentation.artist.components.ViewArtistTopBar
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewArtistCompactScreen(
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
                LoadingType.Loading -> ViewArtistCompactLoadingScreen(
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
                        .background(
                            brush = Brush.verticalGradient(colors = ThemModeChanger.getGradiantBackground())
                        )
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

                            AnimatedVisibility(
                                modifier = Modifier.fillMaxWidth(),
                                visible = state.loadingType is LoadingType.Content,
                                enter = fadeIn(tween(400)) + slideInVertically(
                                    tween(400),
                                    initialOffsetY = { -it }
                                )
                            ) {
                                ViewArtistTopBar(
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
                                            ThemModeChanger.getGradiantBackground().last(),
                                            ThemModeChanger.getGradiantBackground().last(),
                                            Color.Transparent,
                                        )
                                    )
                                )
                                .padding(horizontal = MaterialTheme.dimens.medium1)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = state.artist.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = "Total songs: ${state.mostPopularSongs.size}",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary.copy(.7f),
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize
                                )

                                Spacer(Modifier.height(MaterialTheme.dimens.small3))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(.65f),
                                        onClick = {
                                            onAction(ViewArtistUiAction.OnExploreArtist)
                                        },
                                        shape = CircleShape,
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.background
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 5.dp,
                                            pressedElevation = 0.dp
                                        )
                                    ) {
                                        Text(
                                            text = "${stringResource(R.string.explore_artist)} ${state.artist.name}",
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                                .padding(MaterialTheme.dimens.small3)
                                                .padding(horizontal = MaterialTheme.dimens.small3)
                                                .align(Alignment.CenterHorizontally),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Spacer(Modifier.weight(1f))

                                    IconButton(
                                        onClick = {
                                            onAction(ViewArtistUiAction.OnPlayAll(PlayType.SHUFFLE))
                                        }
                                    ) {
                                        Icon(
                                            imageVector = ShuffleIcon,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(.7f)
                                        )
                                    }

                                    Spacer(Modifier.width(MaterialTheme.dimens.small1))

                                    Card(
                                        modifier = Modifier.size(70.dp),
                                        shape = CircleShape,
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 5.dp,
                                            pressedElevation = 0.dp
                                        ),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.background
                                        ),
                                        onClick = {
                                            onAction(ViewArtistUiAction.OnPlayAll(PlayType.DEFAULT))
                                        }
                                    ) {
                                        Icon(
                                            imageVector = PlayIcon,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    items(state.mostPopularSongs) { song ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(75.dp)
                                .padding(horizontal = MaterialTheme.dimens.medium1)
                                .offset {
                                    IntOffset(
                                        y = -(controllerHeight - (controllerHeight / 2)),
                                        x = 0
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = Modifier.aspectRatio(1f),
                                shape = MaterialTheme.shapes.extraSmall,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                )
                            ) {
                                SubcomposeAsyncImage(
                                    model = CacheImageReq.imageReq(
                                        song.poster,
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
                                                strokeWidth = 1.5.dp,
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
                                                imageVector = SongIcon,
                                                contentDescription = null,
                                                modifier = Modifier.fillMaxSize(.7f),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                )
                            }

                            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(.8f),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    text = song.title,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                song.artists?.let {
                                    Text(
                                        text = it,
                                        color = MaterialTheme.colorScheme.primary.copy(.7f),
                                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(Modifier.weight(1f))

                            IconButton(
                                onClick = {
                                    onAction(ViewArtistUiAction.OnSongOptionsToggle(song.id))
                                }
                            ) {
                                Icon(
                                    imageVector = ThreeDotIcon,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(.8f),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(Modifier.height(MaterialTheme.dimens.small1))
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewArtistCompactScreen(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = PREV_DATA,
                onAction = {},
                navigateBack = {}
            )
        }
    }
}