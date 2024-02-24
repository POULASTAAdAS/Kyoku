package com.poulastaa.kyoku.presentation.screen.setup.select_genre.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestGenreTopBar() {
    TopAppBar(
        title = {
            Text(text = "What's Your Test ?")
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Preview
@Composable
private fun Preview() {
    SuggestGenreTopBar()
}