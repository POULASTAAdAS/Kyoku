package com.poulastaa.kyoku.presentation.screen.edit_playlist

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.edit_playlist.EditPlaylistUiEvent
import com.poulastaa.kyoku.data.model.screens.edit_playlist.LoadingStatus
import com.poulastaa.kyoku.presentation.common.CreatePlaylistScreenContent
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun EditPlaylistScreen(
    viewModel: EditPlaylistViewModel = hiltViewModel(),
    id: Long,
    context: Context,
    navigateBack: () -> Unit
) {
    LaunchedEffect(key1 = viewModel.state.isNavigateBack) {
        if (viewModel.state.isNavigateBack) navigateBack.invoke()
    }

    LaunchedEffect(key1 = id) {
        viewModel.loadData(id)
    }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_LONG
                ).show()

                is UiEvent.Navigate -> {
                    navigateBack.invoke()
                }

                else -> Unit
            }
        }
    }

    when (viewModel.state.loadingStatus) {
        LoadingStatus.LOADING -> {
            EditPlaylistLoadingScreen {
                navigateBack.invoke()
            }
        }

        LoadingStatus.NOT_LOADING -> {
            EditPlaylistScreenContent(
                state = viewModel.state,
                navigateBack = {
                    if (viewModel.state.isSearchEnable) viewModel.onEvent(EditPlaylistUiEvent.CancelSearch)
                    else navigateBack.invoke()
                },
                newPlaylistClick = {
                    viewModel.onEvent(EditPlaylistUiEvent.NewPlaylist.NewPlaylistOpen)
                },
                onSearchClick = {
                    viewModel.onEvent(EditPlaylistUiEvent.SearchClick)
                },
                onSearchTextChange = {
                    viewModel.onEvent(EditPlaylistUiEvent.SearchText(it))
                },
                onPlaylistClick = {
                    viewModel.onEvent(EditPlaylistUiEvent.PlaylistClick(it))
                },
                onFavClick = {
                    viewModel.onEvent(EditPlaylistUiEvent.FavClick)
                },
                onDoneClick = {
                    viewModel.onEvent(EditPlaylistUiEvent.DoneClick)
                }
            )
        }

        LoadingStatus.ERR -> navigateBack.invoke()
    }


    AnimatedVisibility(
        visible = viewModel.state.isNewPlaylist,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 400)
        ) + slideInVertically(
            animationSpec = tween(durationMillis = 400),
            initialOffsetY = { it / 2 }
        ),
        exit = fadeOut(
            animationSpec = tween(400)
        ) + slideOutVertically(
            animationSpec = tween(durationMillis = 400),
            targetOffsetY = { it / 2 }
        )
    ) {
        val temp = viewModel.state.isNewPlaylist

        if (temp)
            CreatePlaylistScreenContent(
                modifier = Modifier.padding(top = MaterialTheme.dimens.medium1),
                text = viewModel.state.newPlaylistText,
                onValueChange = {
                    viewModel.onEvent(EditPlaylistUiEvent.NewPlaylist.NewPlaylistNameEnter(it))
                },
                isLoading = false,
                onDoneClick = {
                    viewModel.onEvent(EditPlaylistUiEvent.NewPlaylist.NewPlaylistYes)
                },
                onCancelClick = {
                    viewModel.onEvent(EditPlaylistUiEvent.NewPlaylist.NewPlaylistNo)
                }
            )
    }

    BackHandler {
        if (viewModel.state.isNewPlaylist) viewModel.onEvent(EditPlaylistUiEvent.NewPlaylist.NewPlaylistNo)
        else if (viewModel.state.isSearchEnable) viewModel.onEvent(EditPlaylistUiEvent.CancelSearch)
        else navigateBack.invoke()
    }
}





