package com.poulastaa.kyoku.presentation.screen.setup.select_genre

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.setup.suggest_genre.SuggestGenreUiEvent

@Composable
fun SuggestGenreScreen(
    viewModel: SelectGenreViewModel = hiltViewModel()
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

    Scaffold {
        SelectGenreContent(
            paddingValues = it,
            data = viewModel.state.data,
            maxGrid = viewModel.state.maxGrid,
            onGenreClicked = { name ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                viewModel.onEvent(SuggestGenreUiEvent.OnGenreClick(name))
            }
        )
    }
}