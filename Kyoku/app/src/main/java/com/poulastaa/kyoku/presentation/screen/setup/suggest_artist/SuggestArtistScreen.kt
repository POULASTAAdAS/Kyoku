package com.poulastaa.kyoku.presentation.screen.setup.suggest_artist

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.setup.suggest_artist.SuggestArtistUiEvent
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomOkButton
import com.poulastaa.kyoku.presentation.screen.setup.suggest_artist.components.SuggestArtistTopBar

@Composable
fun SuggestArtistScreen(
    viewModel: SuggestArtistViewModel = hiltViewModel()
) {
    val context = LocalContext.current as Activity

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

    Scaffold(
        topBar = {
            SuggestArtistTopBar()
        },
        floatingActionButton = {
            CustomOkButton(
                text = "continue",
                modifier = Modifier.padding(16.dp),
                loading = viewModel.state.isSendingDataToApi,
                shape = RoundedCornerShape(4.dp),
                onClick = {
                    viewModel.onEvent(SuggestArtistUiEvent.OnContinueClick)
                }
            )
        }
    ) {
        if (viewModel.state.isFirstApiCall)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(60.dp),
                    strokeCap = StrokeCap.Round
                )
            }
        else
            SuggestArtistContent(
                paddingValues = it,
                maxGrid = viewModel.state.maxGrid,
                data = viewModel.state.data,
                isCookie = viewModel.state.isCookie,
                authHeader = viewModel.state.hearValue,
                onArtistClicked = { name ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.onEvent(SuggestArtistUiEvent.OnArtistClick(name))
                }
            )
    }
}