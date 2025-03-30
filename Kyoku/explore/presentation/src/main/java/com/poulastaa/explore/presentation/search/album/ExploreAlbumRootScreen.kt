package com.poulastaa.explore.presentation.search.album

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.explore.domain.model.ExploreAllowedNavigationScreen
import com.poulastaa.explore.presentation.search.ExploreAlbumUiEvent
import com.poulastaa.explore.presentation.search.ExploreAlbumViewmodel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExploreAlbumRootScreen(
    navigate: (ExploreAllowedNavigationScreen) -> Unit,
    navigateBack: () -> Unit,
) {
    val viewmodel = hiltViewModel<ExploreAlbumViewmodel>()

    val context = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(context)
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val state by viewmodel.state.collectAsStateWithLifecycle()
    val album = viewmodel.album.collectAsLazyPagingItems()

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is ExploreAlbumUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is ExploreAlbumUiEvent.NavigateToAlbum -> navigate(
                ExploreAllowedNavigationScreen.Album(
                    event.albumId
                )
            )
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            ExploreAlbumCompactScreen(
                modifier = Modifier.fillMaxWidth(),
                scroll = scroll,
                state = state,
                album = album,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        mediumContent = {
            ExploreAlbumCompactScreen(
                modifier = Modifier.fillMaxWidth(.7f),
                scroll = scroll,
                state = state,
                album = album,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        expandedContent = {

        }
    )
}