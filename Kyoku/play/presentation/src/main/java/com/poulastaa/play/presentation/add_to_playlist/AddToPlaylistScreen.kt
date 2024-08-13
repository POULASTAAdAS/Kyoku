package com.poulastaa.play.presentation.add_to_playlist

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.model.UiPrevPlaylist
import com.poulastaa.play.presentation.add_to_playlist.components.AddToPlaylistFloatingActionButton
import com.poulastaa.play.presentation.add_to_playlist.components.AddToPlaylistHeading
import com.poulastaa.play.presentation.add_to_playlist.components.AddToPlaylistNavigationIcon
import com.poulastaa.play.presentation.add_to_playlist.components.AddToPlaylistTextField
import com.poulastaa.play.presentation.add_to_playlist.components.PlaylistCard
import com.poulastaa.play.presentation.add_to_playlist.components.addToPlaylistTopPart
import kotlin.random.Random

@Composable
fun AddToPlaylistRootScreen(
    viewModel: AddToPlaylistViewModel = hiltViewModel(),
    songId: Long,
    navigateBack: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.loadData(songId)
    }

    AddToPlaylistScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddToPlaylistScreen(
    state: AddToPlaylistUiState,
    onEvent: (AddToPlaylistUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember {
        FocusRequester()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AddToPlaylistHeading(!state.isSearchEnable)

                    AddToPlaylistTextField(
                        visible = state.isSearchEnable,
                        query = state.query,
                        focusRequester = focusRequester,
                        focusManager = focusManager,
                        onValueChange = {
                            onEvent(AddToPlaylistUiEvent.OnSearchQueryChange(it))
                        },
                        onEvent = onEvent
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    AddToPlaylistNavigationIcon(
                        searchEnabled = state.isSearchEnable,
                        onEvent = onEvent,
                        navigateBack = navigateBack
                    )
                }
            )
        },
        floatingActionButton = {
            AddToPlaylistFloatingActionButton(
                isMakingApiCall = state.isMakingApiCall
            ) {
                onEvent(AddToPlaylistUiEvent.OnSaveClick)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(paddingValues),
            contentPadding = PaddingValues(
                bottom = MaterialTheme.dimens.large2 + MaterialTheme.dimens.large1,
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            ),
        ) {
            addToPlaylistTopPart(
                visible = state.isSearchEnable,
                fev = state.favouriteData,
                onEvent = onEvent
            )

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }

            items(state.playlistData.size) { index ->
                PlaylistCard(
                    modifier = Modifier.height(80.dp),
                    header = state.header,
                    data = state.playlistData[index],
                    onClick = {
                        onEvent(AddToPlaylistUiEvent.OnPlaylistClick(state.playlistData[index].playlist.id))
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }
        }
    }

    if (state.isSearchEnable) BackHandler {
        onEvent(AddToPlaylistUiEvent.CancelSearch)
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PreviewD() {
    val data = AddToPlaylistUiState(
        playlistData = (1..14).map {
            UiPlaylistData(
                playlist = UiPrevPlaylist(
                    name = "Playlist $it"
                ),
                selectStatus = UiSelectStatus(
                    new = Random.nextBoolean()
                )
            )
        },
        favouriteData = UiFavouriteData(
            selectStatus = UiSelectStatus(
                new = true
            )
        )
    )

    AppThem {
        AddToPlaylistScreen(
            state = data,
            onEvent = {}) {

        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewS() {
    val data = AddToPlaylistUiState(
        playlistData = (1..4).map {
            UiPlaylistData(
                playlist = UiPrevPlaylist(
                    name = "Playlist $it"
                ),
                selectStatus = UiSelectStatus(
                    new = Random.nextBoolean()
                )
            )
        },
        isSearchEnable = true
    )

    AppThem {
        AddToPlaylistScreen(
            state = data,
            onEvent = {}) {

        }
    }
}

@Preview
@Composable
private fun PreviewL() {
    val data = AddToPlaylistUiState(
        playlistData = (1..4).map {
            UiPlaylistData(
                playlist = UiPrevPlaylist(
                    name = "Playlist $it"
                ),
                selectStatus = UiSelectStatus(
                    new = Random.nextBoolean()
                )
            )
        },
    )

    AppThem {
        AddToPlaylistScreen(
            state = data,
            onEvent = {}) {

        }
    }
}