package com.poulastaa.kyoku.presentation.screen.create_playlist

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.create_playlist.CreatePlaylistUiEvent
import com.poulastaa.kyoku.presentation.common.CreatePlaylistScreenContent
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun CreatePlaylistScreen(
    viewModel: CreatePlaylistViewModel = hiltViewModel(),
    name: String,
    type: String,
    context: Context,
    navigateBack: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.loadData(name = name, typeString = type)
    }

    LaunchedEffect(key1 = viewModel.state.isCriticalErr) {
        if (viewModel.state.isCriticalErr) navigateBack.invoke()
    }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when(event){
                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> Unit
            }
        }
    }

    CreatePlaylistScreenContent(
        modifier = Modifier.padding(top = MaterialTheme.dimens.medium1),
        text = viewModel.state.text,
        onValueChange = {
            viewModel.onEvent(CreatePlaylistUiEvent.EnterName(it))
        },
        isLoading = viewModel.state.isLoading,
        onDoneClick = {
            viewModel.onEvent(CreatePlaylistUiEvent.SaveClicked)
        },
        onCancelClick = navigateBack
    )

    BackHandler {
        navigateBack.invoke()
    }
}