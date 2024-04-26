package com.poulastaa.kyoku.presentation.screen.setup.suggest_genre

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.poulastaa.kyoku.data.model.UiEvent
import com.poulastaa.kyoku.data.model.screens.setup.suggest_genre.SuggestGenreUiEvent
import com.poulastaa.kyoku.presentation.screen.setup.suggest_genre.components.SuggestGenreTopBar
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun SuggestGenreScreen(
    viewModel: SuggestGenreViewModel = hiltViewModel()
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
                else -> Unit
            }
        }
    }

    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            SuggestGenreTopBar()
        },
        floatingActionButton = {
            FilledTonalButton(
                onClick = {
                    viewModel.onEvent(SuggestGenreUiEvent.OnContinueClick)
                },
                modifier = Modifier.padding(MaterialTheme.dimens.small3),
                shape = MaterialTheme.shapes.extraSmall,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
                elevation = ButtonDefaults.filledTonalButtonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                if (viewModel.state.isSendingDataToApi)
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                else Text(text = "Continue")
            }
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
            SuggestGenreContent(
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