package com.poulastaa.play.presentation.create_playlist

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.components.CompactErrorScreen
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.core.presentation.ui.model.UiSong
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.create_playlist.components.CommonHorizontalPager

@Composable
fun CreatePlaylistRootScreen(
    modifier: Modifier = Modifier,
    playlistId: Long,
    viewModel: CreatePlaylistViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = playlistId) {
        if (viewModel.state.generatedData.isEmpty()) viewModel.init(playlistId)
    }

    ObserveAsEvent(viewModel.uiEvent) {
        when (it) {
            is CreatePlaylistUiAction.EmitToast -> Toast.makeText(
                context,
                it.message.asString(context),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    CreatePlaylistScreen(
        modifier = modifier,
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreatePlaylistScreen(
    modifier: Modifier = Modifier,
    state: CreatePlaylistUiState,
    onEvent: (CreatePlaylistUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val horizontalPager = rememberPagerState { state.generatedData.size }

    Scaffold(
        topBar = {
            AnimatedContent(
                targetState = horizontalPager.currentPage != 0,
                label = ""
            ) {
                when (it) {
                    true -> {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = state.generatedData[horizontalPager.currentPage].type.value,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Transparent
                            ),
                            navigationIcon = {
                                AppBackButton {
                                    navigateBack()
                                }
                            }
                        )
                    }

                    false -> {

                    }
                }
            }
        },
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer)
    ) { paddingValues ->
        AnimatedContent(
            targetState = state.loadingState,
            label = "",
        ) { loadingState ->
            when (loadingState) {
                DataLoadingState.LOADING -> {

                }

                DataLoadingState.LOADED -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(paddingValues),
                    ) {
                        HorizontalPager(
                            state = horizontalPager,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            reverseLayout = true
                        ) { index ->
                            AnimatedContent(index != 0, label = "") {
                                when (it) {
                                    true -> CommonHorizontalPager(
                                        state = state.generatedData[index],
                                        header = state.header,
                                        onEvent = onEvent
                                    )

                                    false -> {

                                    }
                                }
                            }
                        }

                        Column {
                            Spacer(Modifier.height(MaterialTheme.dimens.small3))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                PagerIndicator(horizontalPager)
                            }
                        }
                    }
                }

                DataLoadingState.ERROR -> CompactErrorScreen(Modifier.padding(paddingValues))
            }
        }
    }
}

@Composable
fun PagerIndicator(
    pagerState: PagerState
) {
    repeat(pagerState.pageCount) {
        val color = if (pagerState.pageCount - 1 - it == pagerState.currentPage)
            MaterialTheme.colorScheme.primary.copy(.7f)
        else MaterialTheme.colorScheme.onBackground.copy(.3f)

        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(CircleShape)
                .background(color = color)
                .size(10.dp)
        )
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        CreatePlaylistScreen(
            modifier = Modifier.fillMaxSize(),
            state = CreatePlaylistUiState(
                loadingState = DataLoadingState.LOADED,
                generatedData = listOf(
                    CreatePlaylistData(
                        type = CreatePlaylistType.RECENT_HISTORY,
                        list = (1..10).map {
                            UiSong(
                                id = it.toLong(),
                                title = "That Cool title $it",
                                artist = "That Cool Artist $it",
                                coverImage = ""
                            )
                        }
                    ),
                    CreatePlaylistData(
                        type = CreatePlaylistType.YOUR_FAVOURITES,
                        list = (1..10).map {
                            UiSong(
                                id = it.toLong(),
                                title = "That Cool title $it",
                                artist = "That Cool Artist $it",
                                coverImage = ""
                            )
                        }
                    ),
                    CreatePlaylistData(
                        type = CreatePlaylistType.SUGGESTED_FOR_YOU,
                        list = (1..10).map {
                            UiSong(
                                id = it.toLong(),
                                title = "That Cool title $it",
                                artist = "That Cool Artist $it",
                                coverImage = ""
                            )
                        }
                    ),
                    CreatePlaylistData(
                        type = CreatePlaylistType.YOU_MAY_ALSO_LIKE,
                        list = (1..10).map {
                            UiSong(
                                id = it.toLong(),
                                title = "That Cool title $it",
                                artist = "That Cool Artist $it",
                                coverImage = ""
                            )
                        }
                    )
                )
            ),
            onEvent = {},
            navigateBack = {},
        )
    }
}