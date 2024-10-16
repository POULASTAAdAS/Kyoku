package com.poulastaa.play.presentation.view

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.DownloadIcon
import com.poulastaa.core.presentation.designsystem.MusicImage
import com.poulastaa.core.presentation.designsystem.PlayIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ShuffleIcon
import com.poulastaa.core.presentation.designsystem.components.CompactErrorScreen
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.core.presentation.ui.model.ViewUiSong
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.SongDetailsCard
import com.poulastaa.play.presentation.SongDetailsMovableCard
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid
import com.poulastaa.play.presentation.view.components.ViewDataType
import com.poulastaa.play.presentation.view.components.ViewLoadingAnimation
import com.poulastaa.play.presentation.view_edit.ViewEditRootScreen
import com.poulastaa.play.presentation.view_edit.ViewEditUiInfo
import com.poulastaa.play.presentation.view_edit.toViewEditType
import kotlinx.coroutines.delay

@Composable
fun ViewCompactScreen(
    modifier: Modifier = Modifier,
    id: Long,
    type: ViewDataType,
    viewModel: ViewViewModel = hiltViewModel(),
    navigate: (ViewOtherScreen) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = id, key2 = type) {
        viewModel.loadData(id = id, type = type)
    }

    ObserveAsEvent(flow = viewModel.uiEvent) {
        when (it) {
            is ViewUiAction.Navigate -> navigate(it.screen)
            is ViewUiAction.EmitToast -> Toast.makeText(
                context,
                it.message.asString(context),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    ViewScreen(
        modifier = modifier,
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateBack = navigateBack
    )

    if (viewModel.state.isEditEnabled) BackHandler {
        viewModel.onEvent(ViewUiEvent.OnEditClose)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewScreen(
    modifier: Modifier = Modifier,
    state: ViewUiState,
    onEvent: (ViewUiEvent) -> Unit,
    navigateBack: () -> Unit,
) {
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val config = LocalConfiguration.current
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
    ) {
        Scaffold(
            topBar = {
                ViewTopBar(
                    scrollBehavior = scroll,
                    isEditable = state.type == ViewDataType.PLAYLIST ||
                            state.type == ViewDataType.FEV,
                    onEditClick = {
                        onEvent(ViewUiEvent.OnEditOpen)
                    },
                    navigateBack = navigateBack,
                )
            }
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

                    DataLoadingState.LOADED -> {
                        if (state.type == ViewDataType.PLAYLIST && state.data.listOfSong.isEmpty()) Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .padding(innerPadding)
                                .padding(horizontal = MaterialTheme.dimens.medium1)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 10.dp
                                ),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .size(240.dp)
                            ) {
                                Image(
                                    painter = MusicImage,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(MaterialTheme.dimens.large1),
                                    colorFilter = ColorFilter.tint(
                                        color = MaterialTheme.colorScheme.onBackground.copy(.2f)
                                    )
                                )
                            }

                            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                            Text(
                                text = state.data.name,
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(Modifier.weight(.4f))

                            Text(
                                text = stringResource(R.string.build_your_playlist),
                                fontWeight = FontWeight.SemiBold,
                            )

                            Spacer(Modifier.height(MaterialTheme.dimens.small3))

                            FilledTonalButton(
                                onClick = {
                                    onEvent(ViewUiEvent.OnCreatePlaylistClick(state.data.id))
                                },
                                modifier = Modifier
                                    .fillMaxWidth(.5f),
                                elevation = ButtonDefaults.filledTonalButtonElevation(
                                    defaultElevation = 8.dp
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.explore),
                                    modifier = Modifier.padding(MaterialTheme.dimens.small1),
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 2.sp
                                )
                            }

                            Spacer(Modifier.weight(1f))
                        }
                        else LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .padding(innerPadding)
                                .nestedScroll(scroll.nestedScrollConnection),
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
                                            urls = if (state.type == ViewDataType.ALBUM) listOf(
                                                state.data.urls.getOrElse(index = 0) { "" }
                                            ) else state.data.urls
                                        )
                                    }
                                }
                            }

                            item {
                                Column(
                                    modifier = Modifier
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
                                Spacer(Modifier.height(MaterialTheme.dimens.small2))
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
                                                width = 2.dp,
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
                                            tint = MaterialTheme.colorScheme.primary.copy(.8f),
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }

                            items(state.data.listOfSong) { song ->
                                if (state.isSavedData) SongDetailsMovableCard(
                                    modifier = Modifier.clickable {
                                        onEvent(ViewUiEvent.OnSongClick(song.id))
                                    },
                                    header = state.header,
                                    song = song,
                                    list = state.threeDotOperations,
                                    onMove = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onEvent(ViewUiEvent.OnMoveClick(song.id))
                                    },
                                    onThreeDotOpenClick = {
                                        onEvent(ViewUiEvent.OnThreeDotClick(song.id))
                                    },
                                    onThreeDotOperationClick = {
                                        onEvent(ViewUiEvent.OnThreeDotItemClick(song.id, it))
                                    },
                                    onThreeDotClose = {
                                        onEvent(ViewUiEvent.OnThreeDotClose(song.id))
                                    }
                                )
                                else SongDetailsCard(
                                    modifier = Modifier.clickable {
                                        onEvent(ViewUiEvent.OnSongClick(song.id))
                                    },
                                    header = state.header,
                                    song = song,
                                    list = state.threeDotOperations,
                                    onThreeDotOpenClick = {
                                        onEvent(ViewUiEvent.OnThreeDotClick(song.id))
                                    },
                                    onThreeDotOperationClick = {
                                        onEvent(ViewUiEvent.OnThreeDotItemClick(song.id, it))
                                    },
                                    onThreeDotClose = {
                                        onEvent(ViewUiEvent.OnThreeDotClose(song.id))
                                    }
                                )
                            }
                        }
                    }

                    DataLoadingState.ERROR -> CompactErrorScreen()
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.isEditEnabled,
            enter = fadeIn() + slideInVertically(tween(400)) { it },
            exit = fadeOut() + slideOutVertically { it }
        ) {
            val temp = remember { state.isEditEnabled }

            if (temp) ViewEditRootScreen(
                info = ViewEditUiInfo(
                    id = state.data.id,
                    name = state.data.name,
                    type = state.type.toViewEditType(),
                ),
                onExploreClick = {
                    onEvent(ViewUiEvent.OnCreatePlaylistClick(state.data.id))
                },
                navigateBack = {
                    onEvent(ViewUiEvent.OnEditClose)
                }
            )
        }
    }
}

@Composable
private fun CustomButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
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
                    name = "Playlist",
                    listOfSong = (1..10).map {
                        ViewUiSong(
                            id = it.toLong(),
                            name = "Song $it",
                            artist = "Artist $it"
                        )
                    }
                ),
                topBarTitle = "Playlist",
                loadingState = loading,
                type = ViewDataType.PLAYLIST
            ), onEvent = {}
        ) {

        }
    }
}