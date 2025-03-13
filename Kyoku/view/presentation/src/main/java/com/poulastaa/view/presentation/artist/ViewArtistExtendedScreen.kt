package com.poulastaa.view.presentation.artist

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.UserIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.view.presentation.artist.components.ViewArtistErrorScreen
import com.poulastaa.view.presentation.artist.components.ViewArtistExtendedLoadingScreen
import com.poulastaa.view.presentation.artist.components.ViewArtistTopBar
import com.poulastaa.view.presentation.components.ViewControllerCard
import com.poulastaa.view.presentation.components.ViewSongCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewArtistExtendedScreen(
    state: ViewArtistUiState,
    onAction: (ViewArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        AnimatedContent(targetState = state.loadingType) { loadingState ->
            when (loadingState) {
                LoadingType.Loading -> ViewArtistExtendedLoadingScreen(
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

                LoadingType.Content -> Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(.4f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
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
                                .padding(top = MaterialTheme.dimens.medium2),
                            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                            artist = state.artist,
                            onAction = onAction,
                            navigateBack = navigateBack
                        )

                        Spacer(Modifier.weight(1f))

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = MaterialTheme.dimens.medium1)
                                .padding(start = MaterialTheme.dimens.medium1),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize(.5f)
                                    .aspectRatio(1f),
                                shape = MaterialTheme.shapes.extraSmall,
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
                                                strokeWidth = 2.dp,
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

                            ViewControllerCard(
                                state = state,
                                textAlignment = Alignment.CenterHorizontally,
                                onAction = onAction
                            )
                        }

                        Spacer(Modifier.weight(1f))
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = MaterialTheme.dimens.medium1)
                            .padding(end = MaterialTheme.dimens.medium1),
                        shape = MaterialTheme.shapes.extraSmall,
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentPadding = PaddingValues(vertical = MaterialTheme.dimens.small3)
                        ) {
                            items(state.mostPopularSongs) { song ->
                                ViewSongCard(
                                    controllerHeight = 0,
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
    }
}


@Preview(
    widthDp = 1024,
    heightDp = 540
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 1280,
    heightDp = 740
)
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewArtistExtendedScreen(
                state = PREV_DATA,
                onAction = {},
                navigateBack = {}
            )
        }
    }
}