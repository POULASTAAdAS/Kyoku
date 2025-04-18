package com.poulastaa.main.presentation.home

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.designsystem.model.UiPreSong
import com.poulastaa.core.presentation.designsystem.model.UiPrevAlbum
import com.poulastaa.core.presentation.designsystem.model.UiPrevArtist
import com.poulastaa.main.domain.model.MainAllowedNavigationScreens
import com.poulastaa.main.presentation.components.UiSaveItemType
import com.poulastaa.main.presentation.components.UiSavedItem
import com.poulastaa.main.presentation.home.mapper.toMainAllowedNavigationScreensHome

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun HomeRootScreen(
    viewmodel: HomeViewmodel,
    topBarScroll: TopAppBarScrollBehavior,
    navigate: (MainAllowedNavigationScreens) -> Unit,
) {
    val activity = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(activity)

    val state by viewmodel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is HomeUiEvent.EmitToast -> Toast.makeText(
                activity,
                event.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            else -> navigate(event.toMainAllowedNavigationScreensHome())
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            HomeCompactScreen(
                state = state,
                topBarScroll = topBarScroll,
                onAction = viewmodel::onAction
            )
        },
        mediumContent = {
            HomeMediumScreen(
                state = state,
                topBarScroll = topBarScroll,
                onAction = viewmodel::onAction
            )
        },
        expandedContent = {
            HomeExpandedScreen(
                state = state,
                topAppBarScroll = topBarScroll,
                onAction = viewmodel::onAction
            )
        }
    )
}

internal val prevData = HomeUiState(
    savedItems = listOf(
        UiSavedItem(
            id = 1,
            name = "Saved Item 1",
            posters = listOf(""),
            type = UiSaveItemType.ALBUM
        ),
        UiSavedItem(
            id = 1,
            name = "Saved Item 1",
            posters = listOf(""),
            type = UiSaveItemType.ARTIST
        ),
        UiSavedItem(
            id = 4,
            name = "Saved Item 1",
            posters = listOf(""),
            type = UiSaveItemType.PLAYLIST
        ),
        UiSavedItem(
            id = 5,
            name = "Saved Item 1",
            posters = listOf("", "", "", ""),
            type = UiSaveItemType.PLAYLIST
        )
    ),
    staticData = UiHomeData(
        popularSongMix = (1..10).map { id ->
            UiPreSong(
                id = id.toLong(),
                title = "Song 1",
            )
        },
        popularSongFromYourTime = (1..10).map {
            UiPreSong(
                id = it.toLong(),
                title = "Song 1",
            )
        },
        favouriteArtistMix = (1..10).map {
            UiPreSong(
                id = it.toLong(),
                title = "Song 1",
            )
        },
        popularAlbum = (1..10).map {
            UiPrevAlbum(
                id = it.toLong(),
                name = "Album $it",
            )
        },
        suggestedArtist = (1..10).map {
            UiPrevArtist(
                id = it.toLong(),
                name = "Artist $it",
            )
        },
        popularArtistSong = (1..10).map { artistId ->
            UiArtistWithSong(
                artist = UiPrevArtist(
                    id = artistId.toLong(),
                    name = "Artist $artistId",
                ),
                songs = (1..10).map { songId ->
                    UiPreSong(
                        id = songId.toLong(),
                        title = "Song $songId",
                    )
                }
            )
        }
    )
)