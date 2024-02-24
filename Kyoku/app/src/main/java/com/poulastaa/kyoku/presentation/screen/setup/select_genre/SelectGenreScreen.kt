package com.poulastaa.kyoku.presentation.screen.setup.select_genre

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SelectGenreScreen(
    viewModel: SelectGenreViewModel = hiltViewModel()
) {
    val context = LocalContext.current as Activity

    
}