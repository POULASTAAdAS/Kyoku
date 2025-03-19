package com.poulastaa.view.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.PlayType
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.view.presentation.model.UiViewPrevSong

@Composable
internal fun ViewScreenWrapperExtended(
    data: List<UiViewPrevSong>,
    name: String,
    cover: String,
    covers: List<String>? = null,
    totalSongs: Int,
    loadingType: LoadingType,
    isTypeArtist: Boolean,
    isNotAlbum: Boolean,
    songCardHeight: Dp = 90.dp,
    onExplore: () -> Unit = {},
    play: (type: PlayType) -> Unit,
    onSongClick: (clickType: ItemClickType, songId: SongId) -> Unit,
    onSongThreeDotClick: (songId: SongId) -> Unit,
    topBar: @Composable () -> Unit,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        AnimatedContent(targetState = loadingType) { loadingState ->
            when (loadingState) {
                LoadingType.Loading -> ViewExtendedLoadingScreen(
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
                        topBar()

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
                                ViewCoverCard(isNotAlbum, isTypeArtist, covers, cover)
                            }

                            ViewControllerCard(
                                name = name,
                                totalSongs = totalSongs,
                                isArtist = isTypeArtist,
                                onExplore = onExplore,
                                play = play
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
                            items(data) { song ->
                                ViewSongCard(
                                    controllerHeight = 0,
                                    song = song,
                                    cardHeight = songCardHeight,
                                    onSongClick = onSongClick,
                                    onSongThreeDotClick = onSongThreeDotClick
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