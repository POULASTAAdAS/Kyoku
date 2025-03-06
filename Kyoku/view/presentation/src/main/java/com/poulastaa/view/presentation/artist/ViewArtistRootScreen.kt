package com.poulastaa.view.presentation.artist

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.UiPreSong
import com.poulastaa.view.domain.model.ViewArtistAllowedNavigationScreen
import kotlin.random.Random

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ViewArtistRootScreen(
    artistId: ArtistId,
    navigate: (ViewArtistAllowedNavigationScreen) -> Unit,
    navigateBack: () -> Unit,
) {
    val viewmodel = hiltViewModel<ViewArtistViewmodel>()
    LaunchedEffect(artistId) {
        viewmodel.init(artistId)
    }
    val context = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(context)
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()
    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is ViewArtistUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_SHORT
            ).show()

            is ViewArtistUiEvent.Navigate -> navigate(event.screen)
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            ViewArtistCompactScreen(
                scroll = scroll,
                state = state,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        mediumContent = {

        },
        expandedContent = {

        }
    )
}

internal val PREV_DATA = ViewArtistUiState(
    loadingType = LoadingType.CONTENT,
    artist = UiViewArtist(
        name = "Artist Name",
        isFollowing = Random.nextBoolean()
    ),
    mostPopularSongs = (1..10).map {
        UiPreSong(
            id = it.toLong(),
            title = "Song $it",
        )
    }
)