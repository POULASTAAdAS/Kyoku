package com.poulastaa.play.presentation.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.DownloadIcon
import com.poulastaa.core.presentation.designsystem.PlayIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.SadIcon
import com.poulastaa.core.presentation.designsystem.ShuffleIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.model.ViewUiSong
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid
import com.poulastaa.play.presentation.view.components.ViewDataType
import com.poulastaa.play.presentation.view.components.ViewItemCard
import com.poulastaa.play.presentation.view.components.ViewLoadingAnimation
import kotlinx.coroutines.delay

@Composable
fun ViewCompactScreen(
    modifier: Modifier = Modifier,
    id: Long,
    type: ViewDataType,
    viewModel: ViewViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    LaunchedEffect(key1 = id, key2 = type) {
        viewModel.loadData(id = id, type = type)
    }

    ViewScreen(
        modifier = modifier,
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewScreen(
    modifier: Modifier = Modifier,
    state: ViewUiState,
    onEvent: (ViewUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val config = LocalConfiguration.current

    Scaffold(
        topBar = {
            ViewTopBar(
                title = state.topBarTitle,
                scrollBehavior = scroll,
                navigateBack = navigateBack
            )
        },
        modifier = modifier
    ) { innerPadding ->
        AnimatedContent(
            targetState = state.loadingState,
            label = "view Animated Content",
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(800)
                ) togetherWith fadeOut(animationSpec = tween(800))
            }
        ) { isDataLoading ->
            when (isDataLoading) {
                DataLoadingState.LOADING -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(MaterialTheme.dimens.medium1)
                        .verticalScroll(rememberScrollState())
                        .nestedScroll(scroll.nestedScrollConnection),
                    content = { ViewLoadingAnimation() }
                )

                DataLoadingState.LOADED -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(innerPadding)
                        .nestedScroll(scroll.nestedScrollConnection)
                        .navigationBarsPadding(),
                    contentPadding = PaddingValues(bottom = MaterialTheme.dimens.medium1),
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .then(
                                    if (config.screenWidthDp < 600)
                                        Modifier.padding(horizontal = MaterialTheme.dimens.medium1)
                                    else Modifier
                                        .aspectRatio(1.6f)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(MaterialTheme.dimens.large2),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 10.dp
                                ),
                                shape = MaterialTheme.shapes.small
                            ) {
                                ImageGrid(
                                    header = state.header,
                                    urls = state.data.urls
                                )
                            }
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.dimens.medium1)
                        ) {
                            Text(
                                text = state.data.name,
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = "${state.data.listOfSong.size} songs",
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                color = MaterialTheme.colorScheme.onBackground.copy(.7f)
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.dimens.medium1),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomButton(
                                imageVector = DownloadIcon,
                                modifier = Modifier
                                    .border(
                                        width = 1.5.dp,
                                        color = MaterialTheme.colorScheme.primary.copy(.5f),
                                        shape = CircleShape
                                    )
                                    .size(35.dp)
                            ) {
                                onEvent(ViewUiEvent.OnDownloadClick)
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            CustomButton(imageVector = ShuffleIcon) {
                                onEvent(ViewUiEvent.OnShuffleClick)
                            }

                            Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                            IconButton(
                                onClick = {
                                    onEvent(ViewUiEvent.OnPlayClick)
                                },
                                modifier = Modifier.size(70.dp)
                            ) {
                                Icon(
                                    imageVector = PlayIcon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary.copy(.6f),
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    items(state.data.listOfSong) {
                        ViewItemCard(
                            modifier = Modifier.clickable {
                                onEvent(ViewUiEvent.OnSongClick(it.id))
                            },
                            header = state.header,
                            song = it,
                            onMove = {
                                onEvent(ViewUiEvent.OnMoveClick(it.id))
                            },
                            onThreeDotCLick = {
                                onEvent(ViewUiEvent.OnThreeDotClick(it.id))
                            }
                        )
                    }
                }

                DataLoadingState.ERROR -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(horizontal = MaterialTheme.dimens.medium3),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = SadIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(MaterialTheme.dimens.large1),
                        tint = MaterialTheme.colorScheme.primary.copy(.5f)
                    )

                    Card(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 10.dp
                        ),
                        shape = MaterialTheme.shapes.extraSmall,
                        colors = CardDefaults.cardColors(
                            contentColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.error_something_went_wrong),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(MaterialTheme.dimens.medium1),
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(.6f),
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    var loading by remember {
        mutableStateOf(DataLoadingState.LOADED)
    }

    LaunchedEffect(key1 = Unit) {
        delay(2000)
        loading = DataLoadingState.ERROR
    }

    AppThem {
        ViewScreen(
            state = ViewUiState(
                data = ViewUiData(
                    listOfSong = (1..10).map {
                        ViewUiSong(
                            name = "Name: $it",
                            artist = "Artist: $it"
                        )
                    },
                    name = "Playlist"
                ),
                topBarTitle = "Playlist",
                loadingState = loading
            ), onEvent = {}) {

        }
    }
}