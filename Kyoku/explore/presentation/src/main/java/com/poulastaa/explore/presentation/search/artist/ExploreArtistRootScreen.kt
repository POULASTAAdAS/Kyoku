package com.poulastaa.explore.presentation.search.artist

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ExploreArtistRootScreen(
    navigateToArtist: (artistId: ArtistId) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(context)
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val viewmodel = hiltViewModel<ExploreArtistViewmodel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val artist = viewmodel.artist.collectAsLazyPagingItems()

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is ExploreArtistUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is ExploreArtistUiEvent.NavigateToArtist -> navigateToArtist(event.artistId)
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            ExploreArtistScreen(
                modifier = Modifier.fillMaxWidth(),
                scroll = scroll,
                itemCount = 3,
                state = state,
                artist = artist,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack,
            )
        },
        mediumContent = {
            ExploreArtistScreen(
                modifier = Modifier.fillMaxWidth(.7f),
                scroll = scroll,
                itemCount = 4,
                state = state,
                artist = artist,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack,
            )
        },
        expandedContent = {
            ExploreArtistScreen(
                modifier = Modifier.fillMaxWidth(.7f),
                searchBarWidth = .7f,
                scroll = scroll,
                itemCount = 6,
                state = state,
                artist = artist,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack,
            )
        }
    )
}