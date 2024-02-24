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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.setup.spotify_playlist.GetSpotifyPlaylistUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyPlaylistScreen(
    viewModel: SpotifyPlaylistViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> Unit
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

    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

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
            uiPlaylist = viewModel.state.listOfPlaylist,
            isCookie = viewModel.dsState.isCookie,
            headerValue = viewModel.dsState.tokenOrCookie,
            link = viewModel.state.link,
            onValueChange = {
                viewModel.onEvent(GetSpotifyPlaylistUiEvent.OnLinkEnter(it))
            },
            supportingText = viewModel.state.linkSupportingText,
            isError = viewModel.state.isLinkError,
            isLoading = viewModel.state.isMakingApiCall,
            isFirstPlaylist = viewModel.state.isFirstPlaylist,
            onPlaylistClick = {
                viewModel.onEvent(GetSpotifyPlaylistUiEvent.OnPlaylistClick(it))
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            onAddClick = {
                viewModel.onEvent(GetSpotifyPlaylistUiEvent.OnAddButtonClick)
                focusManager.clearFocus()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            onSkipClick = {
                viewModel.onEvent(GetSpotifyPlaylistUiEvent.OnSkipClick)
            },
            onContinueClick = {
                viewModel.onEvent(GetSpotifyPlaylistUiEvent.OnContinueClick)
            }
        )
    }
}