package com.poulastaa.auth.presentation.intro

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.poulastaa.auth.presentation.intro.components.IntroUiState

@Composable
fun IntroRootScreen(
    viewmodel: IntroViewmodel = hiltViewModel(),
    navigate: () -> Unit,
) {
    IntroHorizontalScreen(
        state = IntroUiState()
    )
}