package com.poulastaa.kyoku.presentation.screen.view_artist

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ViewArtistScreen(
    id: Long,
    viewModel: ViewArtistViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    navigate: (UiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(key1 = viewModel.state.isErr) {
        if (viewModel.state.isErr) navigateBack.invoke()
    }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    navigate.invoke(event)
                }

                is UiEvent.NavigateWithData -> {
                    navigate.invoke(event)
                }

                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {

        }
    ) {
        if (viewModel.state.isLoading)
            ViewArtistLoading(it)
        else
            ViewArtistContent(
                paddingValues = it,
                data = viewModel.state.artist,
                onClick = viewModel::onEvent
            )
    }
}

