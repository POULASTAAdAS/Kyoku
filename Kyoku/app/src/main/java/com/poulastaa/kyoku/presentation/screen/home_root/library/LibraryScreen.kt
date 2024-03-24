package com.poulastaa.kyoku.presentation.screen.home_root.library

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun LibraryScreen(
    title: String = "Your Library",
    profileUrl: String,
    isCookie: Boolean,
    authHeader: String,
    viewModel: LibraryViewModel = hiltViewModel(),
    isSmallPhone: Boolean,
    context: Context,
    paddingValues: PaddingValues,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
        contentPadding = PaddingValues(
            start = MaterialTheme.dimens.medium1,
            end = MaterialTheme.dimens.medium1
        )
    ) {

    }
}