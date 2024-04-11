package com.poulastaa.kyoku.presentation.screen.edit_playlist

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.edit_playlist.LoadingStatus
import kotlinx.coroutines.CoroutineScope

@Composable
fun EditPlaylistScreen(
    viewModel: EditPlaylistViewModel = hiltViewModel(),
    id: Long,
    type: String,
    context: Context,
    scope: CoroutineScope,
    navigate: (UiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(key1 = id) {
        viewModel.loadData(type, id)
    }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> navigate.invoke(event)

                is UiEvent.NavigateWithData -> navigate.invoke(event)

                is UiEvent.ShowToast -> Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    when (viewModel.state.loadingStatus) {
        LoadingStatus.LOADING -> {

        }

        LoadingStatus.NOT_LOADING -> {

        }

        LoadingStatus.ERR -> navigateBack.invoke()
    }
}





