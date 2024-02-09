package com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist

import android.widget.Toast
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.auth.UiEvent
import com.poulastaa.kyoku.data.model.setup.get_spotify_playlist.GetSpotifyPlaylistUiEvent
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyPlaylistScreen(
    viewModel: SpotifyPlaylistViewModel = hiltViewModel(),
    navigateToBDatePick: (UiEvent.Navigate) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> navigateToBDatePick.invoke(event)
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
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Import Your Spotify Playlist",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        textDecoration = TextDecoration.Underline
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        SpotifyPlaylistScreenContent(
            paddingValues = paddingValues,
            link = viewModel.state.link,
            onValueChange = {
                viewModel.onEvent(GetSpotifyPlaylistUiEvent.OnLinkEnter(it))
            },
            supportingText = viewModel.state.linkSupportingText,
            isError = viewModel.state.isLinkError,
            isLoading = viewModel.state.isMakingApiCall,
            isFirstPlaylist = viewModel.state.isFirstPlaylist,
            onAddClick = {
                viewModel.onEvent(GetSpotifyPlaylistUiEvent.OnAddButtonClick)
            },
            onSkipClick = {
                viewModel.onEvent(GetSpotifyPlaylistUiEvent.OnSkipClick)
            }
        )
    }
}