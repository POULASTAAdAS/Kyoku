package com.poulastaa.add.presentation.playlist

import android.app.Activity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddSongToPlaylistRootScreen(
    navigateBack: () -> Unit
) {
    val viewmodel = hiltViewModel<AddSongToPlaylistViewmodel>()

    val context = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(context)

    val state by viewmodel.state.collectAsStateWithLifecycle()
    val searchData = viewmodel.searchData.collectAsLazyPagingItems()

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            AddSongToPlaylistCompactScreen(
                state = state,
                searchData = searchData,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        mediumContent = {
            AddSongToPlaylistCompactScreen(
                state = state,
                searchData = searchData,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        expandedContent = {

        }
    )
}