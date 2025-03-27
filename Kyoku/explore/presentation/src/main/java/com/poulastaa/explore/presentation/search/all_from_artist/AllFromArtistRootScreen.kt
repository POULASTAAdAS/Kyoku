package com.poulastaa.explore.presentation.search.all_from_artist

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
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.explore.domain.model.ExploreAllowedNavigationScreen

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AllFromArtistRootScreen(
    artistId: ArtistId,
    navigate: (ExploreAllowedNavigationScreen) -> Unit,
    navigateBack: () -> Unit,
) {
    val viewmodel = hiltViewModel<AllFromArtistViewModel>()

    LaunchedEffect(artistId) {
        viewmodel.init(artistId)
    }

    val state by viewmodel.state.collectAsStateWithLifecycle()

    val activity = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(activity)
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is AllFromArtistUiEvent.EmitToast -> Toast.makeText(
                activity,
                event.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            is AllFromArtistUiEvent.Navigate -> navigate(event.screen)
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            AllFromArtistCompactScreen(
                modifier = Modifier.fillMaxWidth(),
                scroll = scroll,
                state = state,
                song = viewmodel.song.collectAsLazyPagingItems(),
                album = viewmodel.album.collectAsLazyPagingItems(),
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        mediumContent = {
            AllFromArtistCompactScreen(
                modifier = Modifier.fillMaxWidth(.7f),
                scroll = scroll,
                state = state,
                song = viewmodel.song.collectAsLazyPagingItems(),
                album = viewmodel.album.collectAsLazyPagingItems(),
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        expandedContent = {

        }
    )
}